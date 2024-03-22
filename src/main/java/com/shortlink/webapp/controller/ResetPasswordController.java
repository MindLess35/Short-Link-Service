package com.shortlink.webapp.controller;

import com.shortlink.webapp.dto.request.ResetPasswordDto;
import com.shortlink.webapp.service.ResetPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reset-password")
public class ResetPasswordController {

    private final ResetPasswordService resetPasswordService;

    @PostMapping("/forgot-password")
    public ResponseEntity<HttpStatus> forgotPassword(@RequestParam String emailOrUsername) {
        resetPasswordService.forgotPassword(emailOrUsername);
        return ResponseEntity.accepted().build();
    }

    @PostMapping
    public ResponseEntity<HttpStatus> resetPassword(@RequestBody ResetPasswordDto resetPasswordDto,
                                                    @RequestParam(name = "token") String token) {
        resetPasswordService.resetPassword(resetPasswordDto, token);
        return ResponseEntity.accepted().build();
    }
}

