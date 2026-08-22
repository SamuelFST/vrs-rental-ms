package vrs.rental_ms.integration;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import vrs.rental_ms.dto.vehicle.VehicleRentalUpdateRequestDTO;
import vrs.rental_ms.dto.vehicle.VehicleResponseDTO;
import vrs.rental_ms.enums.VehicleStatus;
import vrs.rental_ms.interceptor.SecurityTokenInterceptor;

@FeignClient(
        name = "VehicleClient",
        url = "${rental-ms.integration.vehicle-client.base-url}",
        configuration = SecurityTokenInterceptor.class
)
public interface VehicleClient {

    @GetMapping("${rental-ms.integration.vehicle-client.find-vehicle-by-id}")
    VehicleResponseDTO findVehicleById(@PathVariable Long vehicleId);

    @PutMapping("${rental-ms.integration.vehicle-client.update-vehicle-status-by-id}")
    void updateVehicleStatus(@PathVariable Long id, @PathVariable VehicleStatus vehicleStatus);

    @PutMapping("${rental-ms.integration.vehicle-client.update-vehicle-rental-data}")
    void updateVehicleRentalData(@PathVariable Long id,
                                 @Valid @RequestBody VehicleRentalUpdateRequestDTO vehicleRentalUpdateRequestDTO);

}
