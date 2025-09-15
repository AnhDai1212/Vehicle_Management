package tipone.buone.api.vehiclemanagement.services.implement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tipone.buone.api.vehiclemanagement.common.constants.MessageConstant;
import tipone.buone.api.vehiclemanagement.enums.ErrorCode;
import tipone.buone.api.vehiclemanagement.exceptions.AppException;
import tipone.buone.api.vehiclemanagement.models.entities.Account;
import tipone.buone.api.vehiclemanagement.models.entities.RefreshToken;
import tipone.buone.api.vehiclemanagement.repositories.RefreshTokenRepository;
import tipone.buone.api.vehiclemanagement.services.RefreshTokenService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private static final int REFRESH_TOKEN_EXPIRY_DAYS = 7;

    @Override
    public String generateRefreshToken(Account account) {
        String newToken = UUID.randomUUID().toString();
        LocalDateTime newExpiry = LocalDateTime.now().plusDays(REFRESH_TOKEN_EXPIRY_DAYS);
        Optional<RefreshToken> oldRefreshToken = refreshTokenRepository.findByAccount(account);
        if(oldRefreshToken.isPresent()){
            RefreshToken rf = oldRefreshToken.get();
            rf.setRefreshToken(newToken);
            rf.setExpiredTime(newExpiry);
            rf.setUpdatedAt(LocalDateTime.now());
            rf.setRevokedAt(null);
            rf.setRevoked(false);
            refreshTokenRepository.save(rf);
        } else {
            RefreshToken newRefreshToken = RefreshToken.builder()
                    .refreshToken(newToken)
                    .expiredTime(newExpiry)
                    .isRevoked(false)
                    .account(account)
                    .build();
            refreshTokenRepository.save(newRefreshToken);
        }
        return newToken;
    }

    @Override
    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> {
                    log.error(MessageConstant.REFRESH_TOKEN_NOT_FOUND, refreshToken);
                    return new AppException(ErrorCode.INVALID_TOKEN);
                });
    }

    @Override
    public RefreshToken findByRefreshTokenWithAccount(String refreshToken) {
        return refreshTokenRepository.findByRefreshTokenWithAccount(refreshToken)
                .orElseThrow(() -> {
                    log.error(MessageConstant.REFRESH_TOKEN_NOT_FOUND, refreshToken);
                    return new AppException(ErrorCode.INVALID_TOKEN);
                });
    }

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken validateRefreshToken(String refreshToken) {
        RefreshToken token = findByRefreshTokenWithAccount(refreshToken);
        if (token.isRevoked()){
            log.error(MessageConstant.REFRESH_TOKEN_IS_REVOKED, refreshToken);
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
        if (token.getExpiredTime().isBefore(LocalDateTime.now())){
            log.error(MessageConstant.REFRESH_TOKEN_IS_EXPIRED, refreshToken);
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
        return token;
    }
}