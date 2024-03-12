package com.shortlink.webapp.validation.validator;

import com.shortlink.webapp.repository.UserRepository;
import com.shortlink.webapp.validation.annotation.Username;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsernameValidator implements ConstraintValidator<Username, String> {

    private final UserRepository userRepository;
    private static final String USERNAME_PATTERN = "^(?!.*\\s)[A-Za-z0-9!@#$%^&*_\\-]{4,32}$";

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        return !userRepository.existsByUsername(username) && username.matches(USERNAME_PATTERN);
    }
}
