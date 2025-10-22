package vrs.rental_ms.mapper;

import org.mapstruct.*;
import vrs.rental_ms.config.properties.PaymentProperties;
import vrs.rental_ms.dto.payment.PaymentRequestDTO;
import vrs.rental_ms.dto.rental.RentalMessageDTO;
import vrs.rental_ms.dto.user.UserAddressResponseDTO;
import vrs.rental_ms.enums.CustomerDocumentType;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @AfterMapping
    default void updatePaymentRequestDTO(@MappingTarget PaymentRequestDTO paymentRequestDTO,
                                         RentalMessageDTO rentalMessageDTO,
                                         UserAddressResponseDTO userAddressResponseDTO) {
        paymentRequestDTO.getCustomer().setType(CustomerDocumentType.fromUserType(rentalMessageDTO.getUser().getUserType()));
        paymentRequestDTO.getCustomer().getAddress().setZipcode(userAddressResponseDTO.getCode());
        paymentRequestDTO.getCustomer().setPhoneNumbers(List.of(userAddressResponseDTO.getContactPhone()));
    }

    @Mapping(target = "apiKey", source = "paymentProperties.apiKey")
    @Mapping(target = "paymentMethod", source = "paymentProperties.allowedPaymentMethod")
    @Mapping(target = "installments", source = "paymentProperties.allowedInstallments")
    @Mapping(target = "amount", source = "rentalMessageDTO.totalPrice")
    @Mapping(target = "cardNumber", source = "rentalMessageDTO.creditCardData.number")
    @Mapping(target = "cardHolderName", source = "rentalMessageDTO.creditCardData.holderName")
    @Mapping(target = "cardExpirationMonth", source = "rentalMessageDTO.creditCardData.expirationMonth")
    @Mapping(target = "cardExpirationYear", source = "rentalMessageDTO.creditCardData.expirationYear")
    @Mapping(target = "cardCvv", source = "rentalMessageDTO.creditCardData.cvv")
    @Mapping(target = "customer.id", source = "rentalMessageDTO.user.id")
    @Mapping(target = "customer.name", source = "rentalMessageDTO.user.name")
    @Mapping(target = "customer.email", source = "rentalMessageDTO.user.email")
    @Mapping(target = "customer.document", source = "rentalMessageDTO.documentNumber")
    @Mapping(target = "customer.address", source = "userAddressResponseDTO")
    PaymentRequestDTO toPaymentRequestDTO(RentalMessageDTO rentalMessageDTO,
                                          UserAddressResponseDTO userAddressResponseDTO,
                                          PaymentProperties paymentProperties);

}
