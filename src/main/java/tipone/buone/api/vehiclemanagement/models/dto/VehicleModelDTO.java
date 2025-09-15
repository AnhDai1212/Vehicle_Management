package tipone.buone.api.vehiclemanagement.models.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tipone.buone.api.vehiclemanagement.common.constants.Constant;
import tipone.buone.api.vehiclemanagement.common.constants.MessageConstant;
import tipone.buone.api.vehiclemanagement.enums.VehicleType;
import tipone.buone.api.vehiclemanagement.validators.annotations.NotBlankOrEmpty;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VehicleModelDTO {
    private String id;

    @NotNull(message = MessageConstant.INVALID_MODEL_CODE)
    @Pattern(regexp = Constant.MODEL_CODE_REGEX, message = MessageConstant.INVALID_MODEL_CODE)
    private String code;

    @NotNull(message = MessageConstant.INVALID_MODEL_NAME)
    @Pattern(regexp = Constant.MODEL_NAME_REGEX, message = MessageConstant.INVALID_MODEL_NAME)
    private String name;

    @NotBlankOrEmpty(fieldName = "Model type")
    private VehicleType type;

    @NotBlankOrEmpty(fieldName = "Customer")
    private String customerId;
    
    private String customerName;

    private String logoPath;

    private String customerEmail;
}