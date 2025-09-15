package tipone.buone.api.vehiclemanagement.models.entities;

import jakarta.persistence.*;
import lombok.*;
import tipone.buone.api.vehiclemanagement.common.base.BaseEntity;

import java.util.Set;

@Entity(name = "customers")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Customer extends BaseEntity {
    @Column(name = "full_name", nullable = false, columnDefinition = "VARCHAR(500)")
    private String fullName;

    @Column(unique = true, nullable = false, columnDefinition = "VARCHAR(500)")
    private String email;

    @Column(unique = true, nullable = false, columnDefinition = "VARCHAR(20)")
    private String phone;

    @Column(name = "tax_number", nullable = false, columnDefinition = "VARCHAR(20)")
    private String taxNumber;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String address;

    @Column(name = "logo_path", columnDefinition = "TEXT")
    private String logoPath;

    @Column(name = "is_active")
    private boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<VehicleModel> vehicleModels;
}