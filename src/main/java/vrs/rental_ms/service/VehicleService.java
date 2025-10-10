package vrs.rental_ms.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import vrs.rental_ms.dto.vehicle.VehicleResponseDTO;
import vrs.rental_ms.enums.VehicleStatus;
import vrs.rental_ms.integration.VehicleClient;

@Service
@AllArgsConstructor
public class VehicleService {

    private final VehicleClient vehicleClient;

    public VehicleResponseDTO findVehicleById(final Long vehicleId) {
        return vehicleClient.findVehicleById(vehicleId);
    }

    public void updateVehicleStatus(final Long vehicleId, final VehicleStatus vehicleStatus) {
        vehicleClient.updateVehicleStatus(vehicleId, vehicleStatus);
    }

}
