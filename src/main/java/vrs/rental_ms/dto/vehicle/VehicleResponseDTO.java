package vrs.rental_ms.dto.vehicle;

import lombok.Data;
import vrs.rental_ms.dto.vehicle.brand.VehicleBrandResponseDTO;
import vrs.rental_ms.dto.vehicle.category.VehicleCategoryResponseDTO;
import vrs.rental_ms.dto.vehicle.model.VehicleModelResponseDTO;
import vrs.rental_ms.dto.vehicle.version.VehicleVersionResponseDTO;
import vrs.rental_ms.enums.VehicleStatus;

import java.math.BigDecimal;

@Data
public class VehicleResponseDTO {

    private Long id;
    private VehicleBrandResponseDTO brand;
    private VehicleModelResponseDTO model;
    private VehicleVersionResponseDTO version;
    private VehicleCategoryResponseDTO category;
    private String year;
    private String type;
    private Long seats;
    private VehicleStatus status;
    private BigDecimal priceBrl;
    private BigDecimal kmDriven;
    private String licensePlate;
    private String imgUrl;

}
