package vrs.rental_ms.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import vrs.rental_ms.RentalMsApplicationTests;
import vrs.rental_ms.document.Rental;
import vrs.rental_ms.dto.rental.RentalFinishMessageDTO;
import vrs.rental_ms.dto.rental.RentalMessageDTO;
import vrs.rental_ms.dto.user.UserAddressResponseDTO;
import vrs.rental_ms.integration.SecurityClient;
import vrs.rental_ms.integration.VehicleClient;
import vrs.rental_ms.repository.RentalRepository;

import java.io.IOException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class RentalServiceTest extends RentalMsApplicationTests {

    @Autowired
    private RentalService rentalService;

    @MockitoBean
    private RentalRepository rentalRepository;

    @MockitoBean
    private MongoTemplate mongoTemplate;

    @MockitoBean
    private SecurityClient securityClient;

    @MockitoBean
    private VehicleClient vehicleClient;

    @MockitoBean
    private RabbitTemplate rabbitTemplate;

    private Rental rental;

    @BeforeEach
    void setup() throws IOException {
        rental = readJsonFileAndConvert("mocks/rental/rental_document.json", Rental.class);
    }

    @Test
    void processRentalWithSuccess() throws Exception {
        var message = readJsonFileAndConvert("mocks/rental/process_rental_message.json", RentalMessageDTO.class);
        var userAddress = readJsonFileAndConvert("mocks/user/find_user_address_by_id_response.json", UserAddressResponseDTO.class);

        when(securityClient.findUserAddressById(anyLong(), anyLong())).thenReturn(userAddress);
        when(rentalRepository.findById(anyString())).thenReturn(Optional.of(rental));
        when(rentalRepository.save(any())).thenReturn(rental);
        doNothing().when(vehicleClient).updateVehicleStatus(anyLong(), any());

        rentalService.processRental(message);

        verify(securityClient, times(1)).findUserAddressById(anyLong(), anyLong());
        verify(rentalRepository, times(1)).findById(anyString());
        verify(rentalRepository, times(1)).save(any());
        verify(vehicleClient, times(1)).updateVehicleStatus(anyLong(), any());
    }

    @Test
    void processRentalFinishWithSuccess() throws Exception {
        var message = readJsonFileAndConvert("mocks/rental/finish_rental_message.json", RentalFinishMessageDTO.class);

        when(rentalRepository.findById(anyString())).thenReturn(Optional.of(rental));
        when(rentalRepository.save(any())).thenReturn(rental);
        doNothing().when(vehicleClient).updateVehicleRentalData(anyLong(), any());
        doNothing().when(rabbitTemplate).convertAndSend(anyString(), any(Object.class));

        rentalService.processRentalFinish(message);

        verify(rabbitTemplate, times(1)).convertAndSend(anyString(), any(Object.class));
        verify(rentalRepository, times(2)).findById(anyString());
        verify(rentalRepository, times(1)).save(any());
        verify(vehicleClient, times(1)).updateVehicleRentalData(anyLong(), any());
    }

    @Test
    void processRentalFinishWithLateFeeWithSuccess() throws Exception {
        var message = readJsonFileAndConvert("mocks/rental/finish_rental_message.json", RentalFinishMessageDTO.class);
        message.setReceivedBackDate(1757635299000L);

        when(rentalRepository.findById(anyString())).thenReturn(Optional.of(rental));
        when(rentalRepository.save(any())).thenReturn(rental);
        doNothing().when(vehicleClient).updateVehicleRentalData(anyLong(), any());
        doNothing().when(rabbitTemplate).convertAndSend(anyString(), any(Object.class));

        rentalService.processRentalFinish(message);

        verify(rabbitTemplate, times(1)).convertAndSend(anyString(), any(Object.class));
        verify(rentalRepository, times(2)).findById(anyString());
        verify(rentalRepository, times(1)).save(any());
        verify(vehicleClient, times(1)).updateVehicleRentalData(anyLong(), any());
    }

    @Test
    void updateRentalWithErrorWithSuccess() {
        when(rentalRepository.findById(anyString())).thenReturn(Optional.of(rental));
        when(rentalRepository.save(any())).thenReturn(rental);

        rentalService.updateRentalWithError("68f0415de743ba1a8056e629");

        verify(rentalRepository, times(1)).findById(anyString());
        verify(rentalRepository, times(1)).save(any());
    }

    @Test
    void updateRentalWithGeneratedContractWithSuccess() {
        when(rentalRepository.findById(anyString())).thenReturn(Optional.of(rental));
        when(rentalRepository.save(any())).thenReturn(rental);

        rentalService.updateRentalWithGeneratedContract("68f0415de743ba1a8056e629", "file_test.pdf");

        verify(rentalRepository, times(1)).findById(anyString());
        verify(rentalRepository, times(1)).save(any());
    }
}