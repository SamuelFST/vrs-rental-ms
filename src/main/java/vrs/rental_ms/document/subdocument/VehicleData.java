package vrs.rental_ms.document.subdocument;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class VehicleData {

    @NotNull(message = "The vehicle id must be informed")
    private Long id;

    @NotBlank(message = "The vehicle brand must be informed")
    private String brand;

    @NotBlank(message = "The vehicle model must be informed")
    private String model;

    @NotBlank(message = "The vehicle version must be informed")
    private String version;

    @NotBlank(message = "The vehicle year must be informed")
    private String year;

    @NotBlank(message = "The vehicle licensePlate must be informed")
    private String licensePlate;

}
