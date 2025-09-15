package tipone.buone.api.vehiclemanagement.models.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tipone.buone.api.vehiclemanagement.common.constants.Constant;
import tipone.buone.api.vehiclemanagement.common.constants.MessageConstant;
import tipone.buone.api.vehiclemanagement.validators.annotations.NotBlankOrEmpty;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {
    private String id;

    @NotNull(message = MessageConstant.INVALID_CUSTOMER_NAME)
    @Pattern(regexp = Constant.CUSTOMER_NAME_REGEX, message = MessageConstant.INVALID_CUSTOMER_NAME)
    private String fullName;

    @Pattern(regexp = Constant.EMAIL_REGEX, message = MessageConstant.INVALID_EMAIL)
    private String email;

    @Pattern(regexp = Constant.PHONE_REGEX, message = MessageConstant.INVALID_PHONE)
    private String phone;

    @Pattern(regexp = Constant.TAX_NUMBER_REGEX, message = MessageConstant.INVALID_TAX_NUMBER)
    private String taxNumber;

    @NotBlankOrEmpty(fieldName = "Address")
    private String address;

    @NotBlankOrEmpty(fieldName = "Dial")
    private String dial;

    @NotBlankOrEmpty(fieldName = "Code")
    private String code;

    private String logoPath;
}