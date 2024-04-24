package com.shortlink.webapp.domain.exception.response;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

//        "timestamp": "2024-04-23T07:40:50.871+00:00",
//        "status": 500,
//        "error": "Internal Server Error",
//        "exception": "com.shortlink.webapp.exception.user.password.InvalidPasswordException",
//        "message": "wrong password",
//        "path": "/api/v1/users/1/change-password"
@SuperBuilder
public class ResponseErrorBody extends BaseResponseErrorBody{

    @Getter
    private final String message;

}
