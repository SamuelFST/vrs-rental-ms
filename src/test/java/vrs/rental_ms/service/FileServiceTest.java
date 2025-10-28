package vrs.rental_ms.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vrs.rental_ms.dto.rental.RentalFileMessageDTO;
import vrs.rental_ms.dto.rental.RentalResponseDTO;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static vrs.rental_ms.RentalMsApplicationTests.readJsonFileAndConvert;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    @InjectMocks
    private FileService fileService;

    @Mock
    private RentalService rentalService;

    @Mock
    private S3Service s3Service;

    @Test
    void generateRentalFile() throws Exception {
        var rental = readJsonFileAndConvert("mocks/rental/rental_document.json", RentalResponseDTO.class);
        var message = new RentalFileMessageDTO().setRentalId("68f0415de743ba1a8056e629");

        when(rentalService.findRentalById(anyString())).thenReturn(rental);
        doNothing().when(rentalService).updateRentalWithGeneratedContract(anyString(), anyString());
        when(s3Service.uploadFile(anyString(), any())).thenReturn("");

        fileService.generateRentalFile(message);

        verify(rentalService, times(1)).findRentalById(anyString());
        verify(rentalService, times(1)).updateRentalWithGeneratedContract(anyString(), anyString());
        verify(s3Service, times(1)).uploadFile(anyString(), any());
    }

}