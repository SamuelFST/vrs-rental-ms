package vrs.rental_ms.service;

import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import vrs.rental_ms.dto.file.FileDataDTO;
import vrs.rental_ms.dto.rental.RentalFileMessageDTO;
import vrs.rental_ms.dto.rental.RentalResponseDTO;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
@AllArgsConstructor
public class FileService {

    private final RentalService rentalService;
    private final S3Service s3Service;

    public FileDataDTO getRentalPdfDownload(final String rentalId) {
        var rental = rentalService.findRentalDocumentById(rentalId);
        var fileStream = s3Service.downloadFile(rental.getGeneratedContract());
        var fileSize = s3Service.getFileSize(rental.getGeneratedContract());

        return new FileDataDTO()
                .setInputStream(fileStream)
                .setFileSize(fileSize)
                .setFilename(rental.getGeneratedContract())
                .setContentType(MediaType.APPLICATION_PDF);
    }

    public void generateRentalFile(final RentalFileMessageDTO rentalFileMessageDTO) {
        var rental = rentalService.findRentalById(rentalFileMessageDTO.getRentalId());
        var rentalPdf = generateRentalPdf(rental);
        var pdfName = "%s_%s_%s.pdf".formatted(rental.getUserData().getName(),
                rental.getVehicleData().getLicensePlate(),
                Instant.now().toEpochMilli());

        s3Service.uploadFile(pdfName, rentalPdf);

        rentalService.updateRentalWithGeneratedContract(rental.getId(), pdfName);
    }

    private static byte[] generateRentalPdf(final RentalResponseDTO rentalResponseDTO) {
        var baos = new ByteArrayOutputStream();
        var writer = new PdfWriter(baos);

        try (var pdf = new PdfDocument(writer); var document = new Document(pdf)) {
            var table = createTable(new float[]{1, 10});

            addHeaderCell(table, "VRS", TextAlignment.LEFT, 24);
            addHeaderCell(table, "Registro de Locação", TextAlignment.CENTER, 18);
            document.add(table);

            addDocumentParagraph(document, "", 10, false);
            addDocumentParagraph(document, "Dados do cliente: ", 10, true);
            addDocumentParagraph(document, "%s: %s".formatted("NOME", rentalResponseDTO.getUserData().getName()), 8, false);
            addDocumentParagraph(document, "%s: %s".formatted("E-MAIL", rentalResponseDTO.getUserData().getEmail()), 8, false);
            addDocumentParagraph(document, "%s: %s".formatted("TIPO DE CLIENTE", rentalResponseDTO.getUserData().getUserType().getDescription()), 8, false);
            addDocumentParagraph(document, "%s: %s".formatted("DOCUMENTO INFORMADO", rentalResponseDTO.getUserData().getDocumentNumber()), 8, false);
            addDocumentParagraph(document, "%s: %s".formatted("TIPO DO DOCUMENTO", rentalResponseDTO.getUserData().getDocumentType()), 8, false);

            addInvisibleDivider(document);

            table = createTable(new float[]{ 1, 1, 1, 1, 1 });
            addDocumentParagraph(document, "Dados do veículo: ", 10, true);
            addTableHeaderCells(table, List.of("MARCA", "MODELO", "VERSÃO", "ANO", "PLACA"));
            addTableCellsValues(table, List.of(
                    rentalResponseDTO.getVehicleData().getBrand(),
                    rentalResponseDTO.getVehicleData().getModel(),
                    rentalResponseDTO.getVehicleData().getVersion(),
                    rentalResponseDTO.getVehicleData().getYear(),
                    rentalResponseDTO.getVehicleData().getLicensePlate()));
            document.add(table);

            addInvisibleDivider(document);

            table = createTable(new float[]{1, 1, 1, 1, 1, 1});
            addDocumentParagraph(document, "Dados da locação: ", 10, true);
            addTableHeaderCells(table, List.of("DATA INÍCIO", "DATA FIM", "DATA DEVOLUÇÃO", "VALOR PAGO", "VALOR ESTORNADO", "DISTÂNCIA PERCORRIDA"));
            addTableCellsValues(table, List.of(
                    formatTimestampToDate(rentalResponseDTO.getStartDate()),
                    formatTimestampToDate(rentalResponseDTO.getEndDate()),
                    formatTimestampToDate(rentalResponseDTO.getReceivedBackDate()),
                    formatToReal(rentalResponseDTO.getFinalPrice()),
                    formatToReal(rentalResponseDTO.getReturnedValue()),
                    "%s %s".formatted(rentalResponseDTO.getRentalKmDriven().toString(), "KMs")));
            document.add(table);

            addInvisibleDivider(document);

            addDocumentParagraph(document, "%s %s".formatted("Essa locação foi paga com o cartão de crédito com final", rentalResponseDTO.getCcLastNumbers()), 12, true);
        }

        return baos.toByteArray();
    }

    private static Table createTable(final float[] values) {
        return new Table(UnitValue.createPercentArray(values))
                .setWidth(UnitValue.createPercentValue(100));
    }

    private static void addTableHeaderCells(final Table table, final List<String> headers) {
        for (var header: headers) {
            table.addHeaderCell(createTableCell(header).setBackgroundColor(new DeviceGray(0.85f)));
        }
    }

    private static void addTableCellsValues(final Table table, final List<String> values) {
        for (var value: values) {
            table.addCell(createTableCell(value));
        }
    }

    private static void addHeaderCell(final Table table,
                                      final String title,
                                      final TextAlignment textAlignment,
                                      final float fontSize) {
        table.addCell(new Cell()
                .add(new Paragraph(title)
                        .setTextAlignment(textAlignment)
                        .setFontSize(fontSize))
                .setTextAlignment(textAlignment)
                .setBorder(null));
    }

    private static void addDocumentParagraph(final Document document,
                                             final String title,
                                             final float fontSize,
                                             final Boolean underlined) {
        document.add(new Paragraph(underlined ? new Text(title).setUnderline() : new Text(title))
                .setTextAlignment(TextAlignment.LEFT)
                .setFontSize(fontSize));
    }

    private static void addInvisibleDivider(final Document document) {
        addDocumentParagraph(document, "", 10, false);
        addDocumentParagraph(document, "", 10, false);
    }

    private static Cell createTableCell(final String content) {
        return new Cell()
                .add(new Paragraph(content))
                .setTextAlignment(TextAlignment.CENTER);
    }

    private static String formatTimestampToDate(final Long timestampMillis) {
        return DateTimeFormatter
                .ofPattern("dd/MM/yyyy")
                .format(Instant.ofEpochMilli(timestampMillis)
                        .atZone(ZoneId.systemDefault()));
    }

    private static String formatToReal(final BigDecimal value) {
        return NumberFormat
                .getCurrencyInstance(new Locale("pt", "BR"))
                .format(value);
    }

}
