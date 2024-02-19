package com.shortlink.webapp.validation.annotation;

import com.shortlink.webapp.validation.validator.UniqueShortLinkNameValidator;
import com.shortlink.webapp.validation.validator.UsernameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = UsernameValidator.class)
public @interface Username {

    String message() default "this username is already taken";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
