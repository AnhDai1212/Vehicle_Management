package tipone.buone.api.vehiclemanagement.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;
import tipone.buone.api.vehiclemanagement.common.base.BaseEntity;

import java.util.Set;

@Entity(name = "countries")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Country extends BaseEntity {
    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String name;

    @Column(nullable = false, columnDefinition = "VARCHAR(5)")
    private String dial;

    @Column(nullable = false, columnDefinition = "VARCHAR(10)")
    private String code;

    @OneToMany(mappedBy = "country")
    private Set<Customer> customers;
}