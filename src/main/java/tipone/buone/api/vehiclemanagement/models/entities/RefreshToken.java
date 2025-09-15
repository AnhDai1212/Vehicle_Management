package tipone.buone.api.vehiclemanagement.models.entities;

import jakarta.persistence.*;
import lombok.*;
import tipone.buone.api.vehiclemanagement.common.base.BaseEntity;

import java.time.LocalDateTime;

@Entity(name = "refresh_tokens")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false, unique = true)
    private Account account;

    @Column(name = "expired_time")
    private LocalDateTime expiredTime;

    @Column(name = "is_revoked")
    private boolean isRevoked;

    @Column(name = "refresh_token", columnDefinition = "VARCHAR(60)", unique = true, nullable = false)
    private String refreshToken;

    @Column(name = "revoked_at")
    private LocalDateTime revokedAt;
}