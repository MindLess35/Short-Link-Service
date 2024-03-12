package com.shortlink.webapp.validation.validator;

import com.shortlink.webapp.repository.LinkRepository;
import com.shortlink.webapp.util.LinkUtil;
import com.shortlink.webapp.validation.annotation.UniqueShortLink;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniqueShortLinkValidator implements ConstraintValidator<UniqueShortLink, String> {

    private final LinkRepository linkRepository;

    @Override
    public boolean isValid(String customLinkName, ConstraintValidatorContext context) {
        if (customLinkName == null)
            return true;
//        context.getDefaultConstraintMessageTemplate().
        return !linkRepository.existsByShortLink(LinkUtil.URI + customLinkName);

//        if (linkRepository.shortLinkNameAlreadyExists(value))
//            throw new RuntimeException();
//        return true;
    }

}
