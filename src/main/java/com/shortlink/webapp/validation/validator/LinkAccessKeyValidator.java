package com.shortlink.webapp.validation.validator;

import com.shortlink.webapp.validation.annotation.LinkAccessKey;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class LinkAccessKeyValidator implements ConstraintValidator<LinkAccessKey, String> {
    public static final String KEY_PATTERN = "^(?!.*\\s)[A-Za-z0-9!@#$%^&*_\\-]{1,64}$";

    @Override
    public boolean isValid(String key, ConstraintValidatorContext context) {
        if (key == null)
            return true;

        return key.matches(KEY_PATTERN);
    }
}
