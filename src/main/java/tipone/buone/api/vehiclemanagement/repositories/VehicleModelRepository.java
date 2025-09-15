package tipone.buone.api.vehiclemanagement.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tipone.buone.api.vehiclemanagement.models.entities.VehicleModel;

import java.util.Optional;

@Repository
public interface VehicleModelRepository extends JpaRepository<VehicleModel, String> {
    Optional<VehicleModel> findByIdAndIsActiveTrue(String id);
    Page<VehicleModel> findAll(Specification<VehicleModel> spec, Pageable pageable);
    @Query("SELECT v FROM vehicle_models v JOIN FETCH v.customer c WHERE v.id = :id AND v.isActive = true AND c.isActive = true")
    Optional<VehicleModel> findByIdWithCustomer(@Param("id") String id);
}