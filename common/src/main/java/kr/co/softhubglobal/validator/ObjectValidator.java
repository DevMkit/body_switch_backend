package kr.co.softhubglobal.validator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import kr.co.softhubglobal.exception.customExceptions.RequestValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ObjectValidator<T> {

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();
    private final MessageSource messageSource;
    public void validate(T objectToValidate) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(objectToValidate);
        if(!constraintViolations.isEmpty()) {
            String errorMessages = String.join(
                    ", ",
                    constraintViolations.stream()
                            .map(constraintViolation -> messageSource.getMessage(constraintViolation.getMessage(), null, Locale.ENGLISH))
                            .collect(Collectors.toSet())
            );
            throw new RequestValidationException(errorMessages);
        }
    }
}