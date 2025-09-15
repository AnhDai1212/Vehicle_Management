package tipone.buone.api.vehiclemanagement.models.entities;

import jakarta.persistence.*;
import lombok.*;
import tipone.buone.api.vehiclemanagement.common.base.BaseEntity;
import tipone.buone.api.vehiclemanagement.enums.RoleType;

import java.util.Set;

@Entity(name = "roles")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role extends BaseEntity {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, columnDefinition = "VARCHAR(255)")
    private RoleType name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<Account> accounts;
}