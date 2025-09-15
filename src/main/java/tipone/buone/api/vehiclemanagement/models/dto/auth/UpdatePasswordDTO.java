package tipone.buone.api.vehiclemanagement.models.dto.auth;

import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import tipone.buone.api.vehiclemanagement.common.constants.Constant;
import tipone.buone.api.vehiclemanagement.common.constants.MessageConstant;

@Data
@RequiredArgsConstructor
public class UpdatePasswordDTO {
    @Pattern(regexp = Constant.PASSWORD_REGEX, message = MessageConstant.INVALID_PASSWORD)
    private String newPassword;
    private String email;
}