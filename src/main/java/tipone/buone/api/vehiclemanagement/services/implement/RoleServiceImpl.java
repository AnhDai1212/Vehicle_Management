package tipone.buone.api.vehiclemanagement.services.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tipone.buone.api.vehiclemanagement.enums.RoleType;
import tipone.buone.api.vehiclemanagement.models.entities.Role;
import tipone.buone.api.vehiclemanagement.repositories.RoleRepository;
import tipone.buone.api.vehiclemanagement.services.RoleService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Optional<Role> getByType(RoleType roleType) {
        return roleRepository.findByName(roleType);
    }
}
