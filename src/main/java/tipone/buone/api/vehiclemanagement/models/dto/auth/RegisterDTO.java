package tipone.buone.api.vehiclemanagement.models.dto.auth;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tipone.buone.api.vehiclemanagement.common.constants.Constant;
import tipone.buone.api.vehiclemanagement.common.constants.MessageConstant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDTO {
    @Pattern(regexp = Constant.EMAIL_REGEX, message = MessageConstant.INVALID_EMAIL)
    private String email;
    @Pattern(regexp = Constant.PASSWORD_REGEX, message = MessageConstant.INVALID_PASSWORD)
    private String password;
}