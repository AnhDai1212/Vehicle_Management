package tipone.buone.api.vehiclemanagement.models.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tipone.buone.api.vehiclemanagement.models.dto.AccountDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationDTO {
    private String accessToken;
    private String refreshToken;
    private AccountDTO account;
}
