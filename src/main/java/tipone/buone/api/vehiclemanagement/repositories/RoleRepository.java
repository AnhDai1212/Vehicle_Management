package tipone.buone.api.vehiclemanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tipone.buone.api.vehiclemanagement.enums.RoleType;
import tipone.buone.api.vehiclemanagement.models.entities.Role;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByName(RoleType name);
}