package com.shortlink.webapp.handler;

import com.shortlink.webapp.exception.base.BadRequestBaseException;
import com.shortlink.webapp.exception.base.ResourceNotFoundException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<?> handleUnexpectedException(Exception e) {
        log.debug(e.getMessage(), e);
        return ResponseEntity.internalServerError().build();
    }

    @ExceptionHandler
    public ResponseEntity<?> handleBadRequestException(BadRequestBaseException e) {
        log.debug(e.getMessage(), e); //todo use AOP for this
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler
    public ResponseEntity<?> handleNotFoundException(ResourceNotFoundException e) {
        log.debug(e.getMessage(), e);
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler
    public ResponseEntity<?> handleJwtException(JwtException e) {
        log.debug(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    //    @ExceptionHandler
//    @ResponseStatus(BAD_REQUEST)
//    public ResponseError handleUserExistException(UserAlreadyExistException e) {
//        log.error(e.getMessage(), e);
//        return new ResponseError(BAD_REQUEST, USER_ALREADY_EXISTS);
//    }
//
//
//    @ExceptionHandler
//    @ResponseStatus(BAD_REQUEST)
//    public ResponseError handleLinkExistException(LinkAlreadyExistException exception) {
//        log.error(exception.getMessage(), exception);
//        return new ResponseError(BAD_REQUEST, LINK_ALREADY_EXISTS);
//    }
//
//    @ExceptionHandler
//    @ResponseStatus(NOT_FOUND)
//    public ResponseError handleLinkExistException(LinkIsNotExistException exception) {
//        log.error(exception.getMessage(), exception);
//        return new ResponseError(NOT_FOUND, LINK_IS_NOT_EXISTS);
//    }
//
//    @ExceptionHandler
//    @ResponseStatus(NOT_FOUND)
//    public ResponseError handleUserNotFoundException(UsernameNotFoundException exception) {
//        log.error(exception.getMessage(), exception);
//        return new ResponseError(NOT_FOUND, USER_IS_NOT_EXISTS);
//    }
//
//    @ExceptionHandler
//    @ResponseStatus(BAD_REQUEST)
//    public ResponseError invalidReceivedObject(MethodArgumentNotValidException exception) {
//        log.error(exception.getMessage(), exception);
//        return new ResponseError(BAD_REQUEST, INVALID_REQUEST_DATA);
//    }
//
//    @ExceptionHandler
//    @ResponseStatus(BAD_REQUEST)
//    public ResponseError invalidPathParameter(ConstraintViolationException exception) {
//        log.error(exception.getMessage(), exception);
//        return new ResponseError(BAD_REQUEST, INVALID_REQUEST_FORMAT);
//    }
//
//    @ExceptionHandler
//    @ResponseStatus(BAD_REQUEST)
//    public ResponseError invalidPathParameter(InvalidSourceLinkException exception) {
//        log.error(exception.getMessage(), exception);
//        return new ResponseError(BAD_REQUEST, INVALID_LINK_FORMAT);
//    }


}
