package vrs.rental_ms.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import vrs.rental_ms.config.security.IsAdmin;
import vrs.rental_ms.config.security.IsClient;
import vrs.rental_ms.dto.rental.*;
import vrs.rental_ms.service.RentalService;

@RestController
@AllArgsConstructor
@RequestMapping(RentalController.RENTAL_ENDPOINT)
@Tag(name = "Rental", description = "Operations for Rentals")
public class RentalController {

    public static final String RENTAL_ENDPOINT = "/rentals";

    private final RentalService rentalService;

    @IsClient
    @GetMapping
    @Operation(summary = "Search all Rentals")
    public Page<RentalResponseDTO> findAllRentals(RentalFilterDTO rentalFilterDTO,
                                                  @PageableDefault Pageable pageable) {
        return rentalService.findAllRentals(rentalFilterDTO, pageable);
    }

    @IsClient
    @GetMapping("/{id}")
    @Operation(summary = "Search for a specific Rental by it's Rental ID")
    public RentalResponseDTO findRentalById(@PathVariable String id) {
        return rentalService.findRentalById(id);
    }

    @IsClient
    @GetMapping("/price")
    @Operation(summary = "Calculate the price of a new Rental")
    public RentalPriceResponse calculateRentalPrice(@RequestParam Long vehicleId,
                                                    @RequestParam Long startDate,
                                                    @RequestParam Long endDate) {
        return rentalService.calculateRentalPrice(vehicleId, startDate, endDate);
    }

    @IsClient
    @PostMapping
    @Operation(summary = "Create a new Rental")
    public RentalResponseDTO createRental(@Valid @RequestBody RentalRequestDTO rentalRequestDTO) {
        return rentalService.createRental(rentalRequestDTO);
    }

    @IsAdmin
    @PutMapping("/{id}")
    @Operation(summary = "Finish a Rental")
    public RentalResponseDTO finishRental(@PathVariable String id,
                                          @Valid @RequestBody RentalFinishRequestDTO rentalFinishRequestDTO) {
        return rentalService.finishRental(id, rentalFinishRequestDTO);
    }

}
