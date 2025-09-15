package tipone.buone.api.vehiclemanagement.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tipone.buone.api.vehiclemanagement.models.entities.Customer;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String>, JpaSpecificationExecutor<Customer> {
    Optional<Customer> findByPhone(String phone);
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByIdAndIsActiveTrue(String id);
    @Query("SELECT c FROM customers c JOIN FETCH c.country WHERE c.id = :id AND c.isActive = true")
    Optional<Customer> findByIdWithCountryAndIsActiveTrue(@Param("id") String id);
    Page<Customer> findAll(Specification<Customer> spec, Pageable pageable);
}