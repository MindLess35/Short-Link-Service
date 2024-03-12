package com.shortlink.webapp.controller;

import com.shortlink.webapp.dto.request.UserCreateEditDto;
import com.shortlink.webapp.dto.request.UserLoginDto;
import com.shortlink.webapp.dto.response.JwtResponseDto;
import com.shortlink.webapp.service.AuthService;
import io.swagger.v3.oas.annotations.headers.Header;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.Map;

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
        return ResponseEntity.ok(authService.login(userLoginDto));

    }

    @GetMapping("/refresh-token")
    public ResponseEntity<JwtResponseDto> refreshToken(HttpServletRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }
}

