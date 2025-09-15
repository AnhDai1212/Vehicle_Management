package tipone.buone.api.vehiclemanagement.models.mappers;

import org.mapstruct.Mapper;
import tipone.buone.api.vehiclemanagement.models.entities.Role;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    default Set<String> mapToNames(Set<Role> roles) {
        if (roles == null || roles.isEmpty()) {
            return Collections.emptySet();
        }
        return roles.stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());
    }
}
