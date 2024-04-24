package com.shortlink.webapp.controller;

import com.shortlink.webapp.dto.user.response.UserCreateDto;
import com.shortlink.webapp.dto.user.response.UserLoginDto;
import com.shortlink.webapp.dto.security.JwtResponseDto;
import com.shortlink.webapp.service.impl.mail.MailVerificationServiceImpl;
import com.shortlink.webapp.service.interfaces.auth.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<JwtResponseDto> register(@RequestBody UserCreateDto userCreateDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.createUser(userCreateDto));
    }

    @PostMapping("/sign-in") //todo add token in header
    public ResponseEntity<JwtResponseDto> authenticate(@RequestBody UserLoginDto userLoginDto) {
        return ResponseEntity.ok(authService.login(userLoginDto));
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<JwtResponseDto> refreshToken(HttpServletRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

}

