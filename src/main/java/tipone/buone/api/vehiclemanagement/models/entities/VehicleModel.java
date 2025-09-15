package tipone.buone.api.vehiclemanagement.models.entities;

import jakarta.persistence.*;
import lombok.*;
import tipone.buone.api.vehiclemanagement.common.base.BaseEntity;
import tipone.buone.api.vehiclemanagement.enums.VehicleType;

@Entity(name = "vehicle_models")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VehicleModel extends BaseEntity {
    @Column(nullable = false, columnDefinition = "VARCHAR(60)")
    private String code;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private VehicleType type;

    @Column(name = "is_active")
    private boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
}