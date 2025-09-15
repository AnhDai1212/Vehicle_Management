package tipone.buone.api.vehiclemanagement.validators.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import tipone.buone.api.vehiclemanagement.validators.annotations.NotBlankOrEmptyOptional;

public class NotBlankOrEmptyEnumOptionalValidator implements ConstraintValidator<NotBlankOrEmptyOptional, Enum<?>> {
    private String fieldName;

    @Override
    public void initialize(NotBlankOrEmptyOptional notBlankOrEmptyOptional) {
        this.fieldName = notBlankOrEmptyOptional.fieldName();
    }

    @Override
    public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
        if (value == null)
            return true;
        if (value.name().trim().isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(fieldName + " must not be blank or empty")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}