package tipone.buone.api.vehiclemanagement.services;

import tipone.buone.api.vehiclemanagement.models.entities.Account;
import tipone.buone.api.vehiclemanagement.models.entities.RefreshToken;

public interface RefreshTokenService {
    String generateRefreshToken(Account account);
    RefreshToken findByRefreshToken(String refreshToken);
    RefreshToken findByRefreshTokenWithAccount(String refreshToken);
    RefreshToken validateRefreshToken(String refreshToken);
    RefreshToken save(RefreshToken refreshToken);
}
