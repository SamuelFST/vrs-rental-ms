package vrs.rental_ms.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import vrs.rental_ms.RentalMsApplicationTests;
import vrs.rental_ms.constants.Constants;
import vrs.rental_ms.document.Rental;
import vrs.rental_ms.service.RentalService;
import vrs.rental_ms.service.S3Service;

import java.io.InputStream;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DownloadControllerTest extends RentalMsApplicationTests {

    @MockitoBean
    private RentalService rentalService;

    @MockitoBean
    private S3Service s3Service;

    @Test
    @WithMockUser(roles = Constants.ADMIN)
    void downloadRentalByRentalIdWithSuccess() throws Exception {
        var rental = readJsonFileAndConvert("mocks/rental/rental_document.json", Rental.class);

        when(rentalService.findRentalDocumentById(anyString())).thenReturn(rental);
        when(s3Service.downloadFile(anyString())).thenReturn(InputStream.nullInputStream());
        when(s3Service.getFileSize(anyString())).thenReturn(150L);

        doRequest(get("/downloads/68f0415de743ba1a8056e629/pdf")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/pdf"))
                .andExpect(header().string("Content-Length", "150"))
                .andExpect(header().string("Content-Disposition", "inline; filename=\"AdminUser_OPQ9012_1761090863078.pdf\""));

    }

}