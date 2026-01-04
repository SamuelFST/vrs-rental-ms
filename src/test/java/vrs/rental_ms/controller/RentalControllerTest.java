package vrs.rental_ms.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import vrs.rental_ms.RentalMsApplicationTests;
import vrs.rental_ms.constants.Constants;
import vrs.rental_ms.document.Rental;
import vrs.rental_ms.dto.rental.RentalFinishRequestDTO;
import vrs.rental_ms.dto.rental.RentalRequestDTO;
import vrs.rental_ms.dto.user.UserResponseDTO;
import vrs.rental_ms.dto.vehicle.VehicleResponseDTO;
import vrs.rental_ms.enums.ErrorMessages;
import vrs.rental_ms.enums.PaymentStatus;
import vrs.rental_ms.enums.RentalStatus;
import vrs.rental_ms.enums.VehicleStatus;
import vrs.rental_ms.integration.SecurityClient;
import vrs.rental_ms.integration.VehicleClient;
import vrs.rental_ms.repository.RentalRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RentalControllerTest extends RentalMsApplicationTests {

    @MockitoBean
    private RentalRepository rentalRepository;

    @MockitoBean
    private MongoTemplate mongoTemplate;

    @MockitoBean
    private VehicleClient vehicleClient;

    @MockitoBean
    private SecurityClient securityClient;

    @MockitoBean
    private RabbitTemplate rabbitTemplate;

    private Rental rental;

    @BeforeEach
    void setup() throws IOException {
        rental = readJsonFileAndConvert("mocks/rental/rental_document.json", Rental.class);
    }

    @Test
    @WithMockUser(roles = Constants.ADMIN)
    void findAllRentalsWithSuccess() throws Exception {
        when(mongoTemplate.find(any(Query.class), any())).thenReturn(List.of(rental));
        when(mongoTemplate.count(any(Query.class), any(Class.class))).thenReturn(1L);

        doRequest(get("/rentals")
                .param("email", "admin@mail.com")
                .param("status", "CLOSED")
                .param("licensePlate", "AAA9999")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "startDate,asc")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(rental.getId()))
                .andExpect(jsonPath("$.content[0].paymentStatus").value(rental.getPaymentStatus().toString()))
                .andExpect(jsonPath("$.content[0].rentalKmDriven").value(rental.getRentalKmDriven().toString()))
                .andExpect(jsonPath("$.content[0].status").value(rental.getStatus().toString()))
                .andExpect(jsonPath("$.content[0].generatedContract").value(rental.getGeneratedContract()))
                .andExpect(jsonPath("$.content[0].ccLastNumbers").value(rental.getCcLastNumbers()))
                .andExpect(jsonPath("$.content[0].addressId").value(rental.getAddressId()))
                .andExpect(jsonPath("$.content[0].startDate").value(rental.getStartDate()))
                .andExpect(jsonPath("$.content[0].endDate").value(rental.getEndDate()))
                .andExpect(jsonPath("$.content[0].receivedBackDate").value(rental.getReceivedBackDate()))
                .andExpect(jsonPath("$.content[0].vehicleData.id").value(rental.getVehicleData().getId()))
                .andExpect(jsonPath("$.content[0].vehicleData.brand").value(rental.getVehicleData().getBrand()))
                .andExpect(jsonPath("$.content[0].vehicleData.model").value(rental.getVehicleData().getModel()))
                .andExpect(jsonPath("$.content[0].vehicleData.version").value(rental.getVehicleData().getVersion()))
                .andExpect(jsonPath("$.content[0].vehicleData.year").value(rental.getVehicleData().getYear()))
                .andExpect(jsonPath("$.content[0].vehicleData.licensePlate").value(rental.getVehicleData().getLicensePlate()))
                .andExpect(jsonPath("$.content[0].userData.id").value(rental.getUserData().getId()))
                .andExpect(jsonPath("$.content[0].userData.name").value(rental.getUserData().getName()))
                .andExpect(jsonPath("$.content[0].userData.email").value(rental.getUserData().getEmail()))
                .andExpect(jsonPath("$.content[0].userData.documentNumber").value(rental.getUserData().getDocumentNumber()))
                .andExpect(jsonPath("$.content[0].userData.documentType").value(rental.getUserData().getDocumentType().toString()))
                .andExpect(jsonPath("$.content[0].userData.userType").value(rental.getUserData().getUserType().toString()));
    }

    @Test
    @WithMockUser(roles = Constants.ADMIN)
    void findRentalByIdWithSuccess() throws Exception {
        when(rentalRepository.findById(anyString())).thenReturn(Optional.of(rental));

        doRequest(get("/rentals/68f0415de743ba1a8056e629")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(rental.getId()))
                .andExpect(jsonPath("$.paymentStatus").value(rental.getPaymentStatus().toString()))
                .andExpect(jsonPath("$.rentalKmDriven").value(rental.getRentalKmDriven().toString()))
                .andExpect(jsonPath("$.status").value(rental.getStatus().toString()))
                .andExpect(jsonPath("$.generatedContract").value(rental.getGeneratedContract()))
                .andExpect(jsonPath("$.ccLastNumbers").value(rental.getCcLastNumbers()))
                .andExpect(jsonPath("$.addressId").value(rental.getAddressId()))
                .andExpect(jsonPath("$.startDate").value(rental.getStartDate()))
                .andExpect(jsonPath("$.endDate").value(rental.getEndDate()))
                .andExpect(jsonPath("$.receivedBackDate").value(rental.getReceivedBackDate()))
                .andExpect(jsonPath("$.vehicleData.id").value(rental.getVehicleData().getId()))
                .andExpect(jsonPath("$.vehicleData.brand").value(rental.getVehicleData().getBrand()))
                .andExpect(jsonPath("$.vehicleData.model").value(rental.getVehicleData().getModel()))
                .andExpect(jsonPath("$.vehicleData.version").value(rental.getVehicleData().getVersion()))
                .andExpect(jsonPath("$.vehicleData.year").value(rental.getVehicleData().getYear()))
                .andExpect(jsonPath("$.vehicleData.licensePlate").value(rental.getVehicleData().getLicensePlate()))
                .andExpect(jsonPath("$.userData.id").value(rental.getUserData().getId()))
                .andExpect(jsonPath("$.userData.name").value(rental.getUserData().getName()))
                .andExpect(jsonPath("$.userData.email").value(rental.getUserData().getEmail()))
                .andExpect(jsonPath("$.userData.documentNumber").value(rental.getUserData().getDocumentNumber()))
                .andExpect(jsonPath("$.userData.documentType").value(rental.getUserData().getDocumentType().toString()))
                .andExpect(jsonPath("$.userData.userType").value(rental.getUserData().getUserType().toString()));
    }

    @Test
    @WithMockUser(roles = Constants.ADMIN)
    void findRentalByIdWithRentalNotFoundError() throws Exception {
        when(rentalRepository.findById(anyString())).thenReturn(Optional.empty());

        doRequest(get("/rentals/12345")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").value(ErrorMessages.RENTAL_NOT_FOUND.getMessage()));
    }

    @Test
    @WithMockUser(roles = Constants.ADMIN, username = "admin@mail.com")
    void calculateRentalPriceWithSuccess() throws Exception {
        var vehicleResponse = readJsonFileAndConvert("mocks/vehicle/find_vehicle_by_id_response.json", VehicleResponseDTO.class);

        when(vehicleClient.findVehicleById(anyLong())).thenReturn(vehicleResponse);

        doRequest(get("/rentals/price")
                .param("vehicleId", "27")
                .param("startDate", "1757035929000")
                .param("endDate", "1757467929000")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(3720.00));
    }

    @Test
    @WithMockUser(roles = Constants.ADMIN, username = "admin@mail.com")
    void createRentalWithSuccess() throws Exception {
        var request = readJsonFileAndConvert("mocks/rental/create_rental_request.json", RentalRequestDTO.class);
        var vehicleResponse = readJsonFileAndConvert("mocks/vehicle/find_vehicle_by_id_response.json", VehicleResponseDTO.class);
        var userResponse = readJsonFileAndConvert("mocks/user/find_user_by_id_response.json", UserResponseDTO.class);
        rental.setStatus(RentalStatus.PENDING);
        rental.setPaymentStatus(PaymentStatus.PROCESSING);

        when(vehicleClient.findVehicleById(anyLong())).thenReturn(vehicleResponse);
        when(securityClient.findUserById(anyLong())).thenReturn(userResponse);
        when(rentalRepository.save(any())).thenReturn(rental);
        doNothing().when(rabbitTemplate).convertAndSend(anyString(), any(Object.class));

        doRequest(post("/rentals")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(rental.getId()))
                .andExpect(jsonPath("$.status").value(rental.getStatus().toString()))
                .andExpect(jsonPath("$.paymentStatus").value(rental.getPaymentStatus().toString()));
    }

    @Test
    @WithMockUser(roles = Constants.ADMIN, username = "admin@mail.com")
    void createRentalWithVehicleNotAvailableError() throws Exception {
        var request = readJsonFileAndConvert("mocks/rental/create_rental_request.json", RentalRequestDTO.class);
        var vehicleResponse = readJsonFileAndConvert("mocks/vehicle/find_vehicle_by_id_response.json", VehicleResponseDTO.class);
        vehicleResponse.setStatus(VehicleStatus.RENTED);

        when(vehicleClient.findVehicleById(anyLong())).thenReturn(vehicleResponse);

        doRequest(post("/rentals")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value(ErrorMessages.VEHICLE_NOT_AVAILABLE.getMessage()));
    }


    @Test
    @WithMockUser(roles = Constants.ADMIN)
    void finishRentalWithSuccess() throws Exception {
        var request = readJsonFileAndConvert("mocks/rental/finish_rental_request.json", RentalFinishRequestDTO.class);
        rental.setStatus(RentalStatus.PROCESSING_CLOSING);

        when(rentalRepository.findById(anyString())).thenReturn(Optional.of(rental));
        when(rentalRepository.save(any())).thenReturn(rental);
        doNothing().when(rabbitTemplate).convertAndSend(anyString(), any(Object.class));

        doRequest(put("/rentals/68f0415de743ba1a8056e629")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(rental.getId()))
                .andExpect(jsonPath("$.status").value(rental.getStatus().toString()));
    }

    @Test
    @WithMockUser(roles = Constants.ADMIN)
    void finishRentalWithRentalAlreadyClosedError() throws Exception {
        var request = readJsonFileAndConvert("mocks/rental/finish_rental_request.json", RentalFinishRequestDTO.class);
        rental.setStatus(RentalStatus.CLOSED);

        when(rentalRepository.findById(anyString())).thenReturn(Optional.of(rental));

        doRequest(put("/rentals/68f0415de743ba1a8056e629")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value(ErrorMessages.RENTAL_ALREADY_CLOSED.getMessage()));
    }

    @Test
    @WithMockUser(roles = Constants.ADMIN)
    void finishRentalWithRentalNotFoundError() throws Exception {
        var request = readJsonFileAndConvert("mocks/rental/finish_rental_request.json", RentalFinishRequestDTO.class);

        when(rentalRepository.findById(anyString())).thenReturn(Optional.empty());

        doRequest(put("/rentals/12345")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").value(ErrorMessages.RENTAL_NOT_FOUND.getMessage()));
    }

}