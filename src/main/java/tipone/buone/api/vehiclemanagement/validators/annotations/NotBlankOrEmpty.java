package tipone.buone.api.vehiclemanagement.validators.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import tipone.buone.api.vehiclemanagement.validators.constraint.NotBlankOrEmptyEnumValidator;
import tipone.buone.api.vehiclemanagement.validators.constraint.NotBlankOrEmptyValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {NotBlankOrEmptyValidator.class, NotBlankOrEmptyEnumValidator.class})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotBlankOrEmpty {
    String message() default "{fieldName} must not be blank or empty";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String fieldName();
}
