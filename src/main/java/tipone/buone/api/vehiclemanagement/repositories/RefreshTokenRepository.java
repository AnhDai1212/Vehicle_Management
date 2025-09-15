package tipone.buone.api.vehiclemanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tipone.buone.api.vehiclemanagement.models.entities.Account;
import tipone.buone.api.vehiclemanagement.models.entities.RefreshToken;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    Optional<RefreshToken> findByAccount(Account account);
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
    @Query("SELECT rt FROM refresh_tokens rt JOIN FETCH rt.account WHERE rt.refreshToken = :refreshToken")
    Optional<RefreshToken> findByRefreshTokenWithAccount(@Param("refreshToken") String refreshToken);
}