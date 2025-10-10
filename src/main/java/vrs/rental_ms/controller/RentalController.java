package vrs.rental_ms.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vrs.rental_ms.config.security.IsClient;
import vrs.rental_ms.dto.rental.RentalRequestDTO;
import vrs.rental_ms.dto.rental.RentalResponseDTO;
import vrs.rental_ms.service.RentalService;

@RestController
@AllArgsConstructor
@RequestMapping(RentalController.RENTAL_ENDPOINT)
@Tag(name = "Rental", description = "Operations for Rentals")
public class RentalController {

    public static final String RENTAL_ENDPOINT = "/rentals";

    private final RentalService rentalService;

    @IsClient
    @PostMapping
    @Operation(summary = "Create a new Rental")
    public RentalResponseDTO createRental(@Valid @RequestBody RentalRequestDTO rentalRequestDTO) {
        return rentalService.createRental(rentalRequestDTO);
    }

}
