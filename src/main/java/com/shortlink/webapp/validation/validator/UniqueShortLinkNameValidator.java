package com.shortlink.webapp.validation.validator;

import com.shortlink.webapp.repository.LinkRepository;
import com.shortlink.webapp.validation.annotation.UniqueShortLinkName;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniqueShortLinkNameValidator implements ConstraintValidator<UniqueShortLinkName, String> {

    private final LinkRepository linkRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
//        context.getDefaultConstraintMessageTemplate().
        return !linkRepository.shortLinkNameAlreadyExists(value);

//        if (linkRepository.shortLinkNameAlreadyExists(value))
//            throw new RuntimeException();
//        return true;
    }

}
