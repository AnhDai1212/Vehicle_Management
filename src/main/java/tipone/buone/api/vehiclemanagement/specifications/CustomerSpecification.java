package tipone.buone.api.vehiclemanagement.specifications;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import tipone.buone.api.vehiclemanagement.models.entities.Customer;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CustomerSpecification {
    public static Specification<Customer> checkDuplicateFields(String currentId, String email, String phone) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (email != null) {
                predicates.add(criteriaBuilder.equal(root.get("email"), email));
            }
            if (phone != null) {
                predicates.add(criteriaBuilder.equal(root.get("phone"), phone));
            }
            if (predicates.isEmpty()) {
                return criteriaBuilder.disjunction();
            }
            Predicate duplicateCondition = criteriaBuilder.or(predicates.toArray(new Predicate[0]));
            Predicate notCurrentRecord = criteriaBuilder.notEqual(root.get("id"), currentId);
            return criteriaBuilder.and(duplicateCondition, notCurrentRecord);
        };
    }
}
