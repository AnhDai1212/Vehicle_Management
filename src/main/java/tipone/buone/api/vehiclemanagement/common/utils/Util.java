package tipone.buone.api.vehiclemanagement.common.utils;

import jakarta.persistence.criteria.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import tipone.buone.api.vehiclemanagement.common.constants.Constant;
import tipone.buone.api.vehiclemanagement.common.constants.MessageConstant;
import tipone.buone.api.vehiclemanagement.enums.ErrorCode;
import tipone.buone.api.vehiclemanagement.exceptions.AppException;
import tipone.buone.api.vehiclemanagement.models.dto.pagination.PaginationDTO;
import tipone.buone.api.vehiclemanagement.models.dto.pagination.SortDTO;
import tipone.buone.api.vehiclemanagement.models.entities.Customer;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Util {
    private static final String ALPHANUMERIC = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "0123456789";
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static String getPrefix(Class<?> className){
        String prefix = Constant.PREFIX_ENTITIES.get(className);
        if(prefix == null){
            throw new IllegalArgumentException(MessageConstant.ERROR_NOT_FOUND_PREFIX_ENTITY);
        }
        return prefix;
    }

    public static String randomAlphaNumber(int length){
        return SECURE_RANDOM.ints(length,0,ALPHANUMERIC.length())
                .mapToObj(ALPHANUMERIC::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
    }

    /**
     * Generates a random numeric string of the specified length using a cryptographically secure random generator.
     * <p>
     * This method draws {@code length} random integers in the range [0, NUMBERS.length()), maps each integer
     * to the corresponding character in the {@code NUMBERS} string (e.g., '0'–'9'), and concatenates them
     * into a single {@code String}.
     * </p>
     *
     * @param length the desired length of the random numeric string; must be non-negative
     * @return a {@code String} of length {@code length} consisting of randomly selected digits
     * @throws IllegalArgumentException if {@code length} is negative
     */
    public static String randomNumbers(int length){
        return SECURE_RANDOM.ints(length,0,NUMBERS.length())
                .mapToObj(NUMBERS::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
    }

    public static Pageable toPageable(List<SortDTO> sortModels, PaginationDTO pagination, Set<String> sortFields) {
        List<Sort.Order> orders = new ArrayList<>();
        if (sortModels != null) {
            for (SortDTO sort : sortModels) {
                if (!sortFields.contains(sort.getField())) {
                    throw new AppException(ErrorCode.VALIDATION_ERROR, "Invalid sort field: " + sort.getField());
                }
                Sort.Direction dir = Sort.Direction.fromString(sort.getSort().name());
                orders.add(new Sort.Order(dir, sort.getField()));
            }
        }
        return PageRequest.of(pagination.getPage(), pagination.getPageSize(), Sort.by(orders));
    }

    public static String buildSearchKeyword(String input) {
        if (input == null || input.isBlank()) return null;
        input = input.trim();
        return "%" + input.toLowerCase()
                .replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_") + "%";
    }

    public static <T> Specification<T> buildSearchSpec(String keyword, Set<String> searchableFields, Set<String> fetchRelations, String isActiveField) {
        return (root, query, cb) -> {
            if (fetchRelations != null && query.getResultType().equals(Customer.class)) {
                for (String relation : fetchRelations) {
                    FetchParent<?, ?> fetch = root;
                    for (String part : relation.split("\\.")) {
                        fetch = fetch.fetch(part, JoinType.LEFT);
                    }
                }
                query.distinct(true);
            }

            List<Predicate> predicates = new ArrayList<>();
            if (isActiveField != null && !isActiveField.isBlank()) {
                predicates.add(cb.isTrue(root.get(isActiveField)));
            }
            if (keyword != null && !keyword.isBlank()) {
                List<Predicate> searchPredicates = new ArrayList<>();
                for (String field : searchableFields) {
                    Path<?> path = root;
                    for (String part : field.split("\\.")) {
                        path = path.get(part);
                    }
                    Expression<String> expression = cb.lower(path.as(String.class));
                    searchPredicates.add(cb.like(expression, keyword, '\\'));
                }
                predicates.add(cb.or(searchPredicates.toArray(new Predicate[0])));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static String getImageIdFromUrl(String url){
        if (url == null || url.isEmpty()) {
            return null;
        }
        String[] parts = url.split("/");
        return parts[parts.length - 1].split("\\.")[0];
    }
}