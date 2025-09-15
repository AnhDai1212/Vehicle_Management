package tipone.buone.api.vehiclemanagement.services;

import tipone.buone.api.vehiclemanagement.models.dto.auth.TokenDTO;

public interface AuthService {
    TokenDTO refreshToken(String refreshToken);
}
