package tipone.buone.api.vehiclemanagement.services;

import tipone.buone.api.vehiclemanagement.enums.RoleType;
import tipone.buone.api.vehiclemanagement.models.entities.Role;

import java.util.Optional;

public interface RoleService {
    Optional<Role> getByType(RoleType roleType);
}
