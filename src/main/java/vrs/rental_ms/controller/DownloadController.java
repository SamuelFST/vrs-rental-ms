package vrs.rental_ms.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vrs.rental_ms.config.security.IsClient;
import vrs.rental_ms.service.FileService;

@RestController
@AllArgsConstructor
@RequestMapping(DownloadController.DOWNLOAD_ENDPOINT)
@Tag(name = "Download", description = "Operations for Downloads")
public class DownloadController {

    public static final String DOWNLOAD_ENDPOINT = "/downloads";

    private final FileService fileService;

    @IsClient
    @GetMapping("/{rentalId}/pdf")
    @Operation(summary = "Download PDF of a specific Rental by it's Rental ID")
    public ResponseEntity<InputStreamResource> downloadRentalByRentalId(@PathVariable String rentalId) {
        var pdfData = fileService.getRentalPdfDownload(rentalId);

        return ResponseEntity.ok()
                .contentType(pdfData.getContentType())
                .contentLength(pdfData.getFileSize())
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.inline()
                                .filename(pdfData.getFilename())
                                .build()
                                .toString())
                .body(new InputStreamResource(pdfData.getInputStream()));
    }

}
