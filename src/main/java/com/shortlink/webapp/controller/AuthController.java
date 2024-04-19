package com.shortlink.webapp.controller;

import com.shortlink.webapp.dto.request.UserCreateDto;
import com.shortlink.webapp.dto.request.UserLoginDto;
import com.shortlink.webapp.dto.response.JwtResponseDto;
import com.shortlink.webapp.service.AuthService;
import com.shortlink.webapp.service.MailVerificationService;
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
    private final MailVerificationService mailVerificationService;
//    private final UserService userService;


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

