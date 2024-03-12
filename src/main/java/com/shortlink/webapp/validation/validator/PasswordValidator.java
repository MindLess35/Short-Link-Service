package com.shortlink.webapp.validation.validator;

import com.shortlink.webapp.validation.annotation.Password;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class PasswordValidator implements ConstraintValidator<Password, String> {

    public static final String PASSWORD_PATTERN = "^(?!.*\\s)[A-Za-z0-9!@#$%^&*_\\-]{8,64}$";

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        return password.matches(PASSWORD_PATTERN);
    }

}
