package com.shortlink.webapp.handler;

import com.shortlink.webapp.exception.base.BadRequestBaseException;
import com.shortlink.webapp.exception.base.ResourceNotFoundException;
import com.shortlink.webapp.exception.response.ResponseErrorBody;
import com.shortlink.webapp.exception.response.ResponseViolationErrorBody;
import com.shortlink.webapp.exception.user.password.InvalidPasswordException;
import com.shortlink.webapp.exception.user.password.PasswordConfirmationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;


//{
//    "timestamp": "2024-04-23T07:40:50.871+00:00",
//        "status": 500,
//        "error": "Internal Server Error",
//        "exception": "com.shortlink.webapp.exception.user.password.InvalidPasswordException",
//        "message": "wrong password",
//        "path": "/api/v1/users/1/change-password"
//}
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class RestExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseErrorBody handleUnexpectedException(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseErrorBody.builder()
                .error(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, BadRequestBaseException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseErrorBody handleBadRequestException(Exception e) {
        log.error(e.getMessage(), e); //todo use AOP for this
        return ResponseErrorBody.builder()
                .error(HttpStatus.BAD_REQUEST)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseErrorBody handleNotFoundException(ResourceNotFoundException e) {
        log.error(e.getMessage(), e);
        return ResponseErrorBody.builder()
                .error(HttpStatus.NOT_FOUND)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler({PasswordConfirmationException.class, InvalidPasswordException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseViolationErrorBody handlePasswordOperationException(Exception e) {
        log.error(e.getMessage(), e);
        ResponseViolationErrorBody result = ResponseViolationErrorBody.builder()
                .error(HttpStatus.BAD_REQUEST)
                .build();

        if (e instanceof PasswordConfirmationException) {
            result.setFieldErrors(Map.of("passwordConfirmation",
                    messageSource.getMessage("password.confirmation", null,
                            LocaleContextHolder.getLocale())));
        } else {
            result.setFieldErrors(Map.of("invalidPassword",
                    messageSource.getMessage("password.invalid", null,
                            LocaleContextHolder.getLocale())));
        }

        return result;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseViolationErrorBody handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        return ResponseViolationErrorBody.builder()
                .fieldErrors(e.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage,
                                (message, anotherMessage) ->
                                        message + messageSource.getMessage("and", null,
                                                LocaleContextHolder.getLocale()) + anotherMessage
                        )))
                .error(HttpStatus.BAD_REQUEST)
                .build();
    }

}
