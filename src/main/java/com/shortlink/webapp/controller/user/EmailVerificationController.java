package com.shortlink.webapp.controller.user;

import com.shortlink.webapp.service.interfaces.mail.MailVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/email")
public class EmailVerificationController {

    private final MailVerificationService mailVerificationService;

    @PostMapping("/{userId}/send-verification-token")
    public ResponseEntity<HttpStatus> sendVerificationToken(@PathVariable("userId") Long userId) {
        mailVerificationService.sendVerificationToken(userId);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/verify-email")
    public ResponseEntity<HttpStatus> verifyEmail(@RequestParam(name = "token") String token) {
        mailVerificationService.verifyMail(token);
        return ResponseEntity.accepted().build();
    }
}

