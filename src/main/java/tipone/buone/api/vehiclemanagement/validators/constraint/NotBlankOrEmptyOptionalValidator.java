package tipone.buone.api.vehiclemanagement.validators.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import tipone.buone.api.vehiclemanagement.validators.annotations.NotBlankOrEmptyOptional;

public class NotBlankOrEmptyOptionalValidator implements ConstraintValidator<NotBlankOrEmptyOptional, String> {
    private String fieldName;

    @Override
    public void initialize(NotBlankOrEmptyOptional notBlankOrEmptyOptional) {
        this.fieldName = notBlankOrEmptyOptional.fieldName();
    }
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        if (value.trim().isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(fieldName + " must not be blank or empty")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}