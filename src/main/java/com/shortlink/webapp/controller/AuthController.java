package com.shortlink.webapp.controller;

import com.shortlink.webapp.dto.request.UserCreateEditDto;
import com.shortlink.webapp.dto.request.UserLoginDto;
import com.shortlink.webapp.dto.response.JwtResponseDto;
import com.shortlink.webapp.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
//    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<JwtResponseDto> register(@RequestBody UserCreateEditDto userCreateEditDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.createUser(userCreateEditDto));

    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> authenticate(@RequestBody UserLoginDto userLoginDto) {
        return ResponseEntity.ok().body(authService.login(userLoginDto));

    }

    @PostMapping("/refresh-token")
    public ResponseEntity<JwtResponseDto> refreshToken(HttpServletRequest request) {
       return ResponseEntity.ok().body(authService.refreshToken(request));
    }
}

