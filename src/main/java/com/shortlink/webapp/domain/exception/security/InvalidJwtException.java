package com.shortlink.webapp.domain.exception.security;

import com.shortlink.webapp.domain.exception.base.BadRequestBaseException;

public class InvalidJwtException extends BadRequestBaseException { //todo replace by JwtException
    public InvalidJwtException(String massage) {
        super(massage);
    }
}
