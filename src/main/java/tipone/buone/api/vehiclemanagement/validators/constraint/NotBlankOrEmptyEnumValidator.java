package tipone.buone.api.vehiclemanagement.validators.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import tipone.buone.api.vehiclemanagement.validators.annotations.NotBlankOrEmpty;

public class NotBlankOrEmptyEnumValidator implements ConstraintValidator<NotBlankOrEmpty, Enum<?>> {
    private String fieldName;

    @Override
    public void initialize(NotBlankOrEmpty notBlankOrEmpty) {
        this.fieldName = notBlankOrEmpty.fieldName();
    }

    @Override
    public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
        if (value == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(fieldName + " must not be blank or empty")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}