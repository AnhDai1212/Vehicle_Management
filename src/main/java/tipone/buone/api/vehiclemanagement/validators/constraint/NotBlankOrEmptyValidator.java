package tipone.buone.api.vehiclemanagement.validators.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import tipone.buone.api.vehiclemanagement.validators.annotations.NotBlankOrEmpty;

public class NotBlankOrEmptyValidator implements ConstraintValidator<NotBlankOrEmpty, String> {
    private String fieldName;

    @Override
    public void initialize(NotBlankOrEmpty notBlankOrEmpty){
        this.fieldName = notBlankOrEmpty.fieldName();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null|| value.trim().isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(fieldName + " must not be blank or empty")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}