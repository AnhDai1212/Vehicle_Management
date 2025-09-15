package tipone.buone.api.vehiclemanagement.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import tipone.buone.api.vehiclemanagement.common.base.BaseEntity;

@Entity(name = "warehouses")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Warehouse extends BaseEntity {
    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String name;
    @Column(name = "is_active")
    private boolean isActive;
}