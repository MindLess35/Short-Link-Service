package com.shortlink.webapp.service.interfaces.auth;

import com.shortlink.webapp.dto.security.JwtResponseDto;
import com.shortlink.webapp.dto.user.response.UserCreateDto;
import com.shortlink.webapp.dto.user.response.UserLoginDto;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    JwtResponseDto createUser(UserCreateDto userCreateDto);

    JwtResponseDto login(UserLoginDto userLoginDto);

    JwtResponseDto refreshToken(HttpServletRequest request);
}
