package com.shortlink.webapp.controller.user;

import com.shortlink.webapp.dto.user.response.ResetPasswordDto;
import com.shortlink.webapp.service.interfaces.user.ResetPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class ResetPasswordController {

    private final ResetPasswordService resetPasswordService;

    @PostMapping("/forgot-password")
    public ResponseEntity<HttpStatus> forgotPassword(@RequestParam String emailOrUsername) {
        resetPasswordService.forgotPassword(emailOrUsername);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<HttpStatus> resetPassword(@RequestBody ResetPasswordDto resetPasswordDto,
                                                    @RequestParam(name = "token") String token) {
        resetPasswordService.resetPassword(resetPasswordDto, token);
        return ResponseEntity.accepted().build();
    }
}

