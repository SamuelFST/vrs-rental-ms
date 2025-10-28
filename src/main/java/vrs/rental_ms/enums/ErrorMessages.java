package vrs.rental_ms.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessages {
    UNAUTHORIZED("Unauthorized User"),
    UNEXPECTED_ERROR("Unexpected exception occurred: "),
    CURRENT_USER_OPERATION_NOT_ALLOWED("Operation not allowed for the current User"),
    TOKEN_NOT_INFORMED("accessToken not informed"),
    TOKEN_GENERATION_ERROR("Error occurred when generating token"),
    TOKEN_VALIDATION_ERROR("Error occurred when validating token"),
    VEHICLE_CATEGORY_NOT_FOUND("VehicleCategory not found with given ID"),
    VEHICLE_BRAND_NOT_FOUND("VehicleBrand not found with given ID"),
    VEHICLE_MODEL_NOT_FOUND("VehicleModel not found with given ID"),
    VEHICLE_VERSION_NOT_FOUND("VehicleVersion not found with given ID"),
    VEHICLE_NOT_FOUND("Vehicle not found with given ID"),
    VEHICLE_NOT_AVAILABLE("Vehicle not available for rental"),
    RENTAL_NOT_FOUND("Rental not found with given ID"),
    RENTAL_ALREADY_CLOSED("Rental with given ID already closed");

    private final String message;
}
