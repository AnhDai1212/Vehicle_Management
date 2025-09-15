package tipone.buone.api.vehiclemanagement.models.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tipone.buone.api.vehiclemanagement.common.constants.Constant;
import tipone.buone.api.vehiclemanagement.common.constants.MessageConstant;
import tipone.buone.api.vehiclemanagement.enums.VehicleType;
import tipone.buone.api.vehiclemanagement.validators.annotations.NotBlankOrEmptyOptional;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VehicleModelUpdateDTO {

    @Pattern(regexp = Constant.MODEL_CODE_REGEX, message = MessageConstant.INVALID_MODEL_CODE)
    private String code;

    @Pattern(regexp = Constant.MODEL_NAME_REGEX, message = MessageConstant.INVALID_MODEL_NAME)
    private String name;

    @NotBlankOrEmptyOptional(fieldName = "Model type")
    private VehicleType type;

    @NotBlankOrEmptyOptional(fieldName = "Customer")
    private String customerId;
}