package com.shortlink.webapp.exception.security;

import com.shortlink.webapp.exception.base.BadRequestBaseException;

public class InvalidJwtException extends BadRequestBaseException { //todo replace by JwtException
    public InvalidJwtException(String massage) {
        super(massage);
    }
}
