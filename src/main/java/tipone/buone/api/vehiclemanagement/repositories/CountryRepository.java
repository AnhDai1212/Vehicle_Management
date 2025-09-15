package tipone.buone.api.vehiclemanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tipone.buone.api.vehiclemanagement.models.entities.Country;

import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, String> {
    Optional<Country> findByDialAndCode(String dial, String code);
}