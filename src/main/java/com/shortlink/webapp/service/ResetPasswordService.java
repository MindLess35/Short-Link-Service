package com.shortlink.webapp.service;

import com.shortlink.webapp.dto.projection.ResetPasswordWithUserProjection;
import com.shortlink.webapp.dto.request.ResetPasswordDto;
import com.shortlink.webapp.entity.ResetPassword;
import com.shortlink.webapp.entity.User;
import com.shortlink.webapp.entity.enums.MailType;
import com.shortlink.webapp.exception.InvalidPasswordException;
import com.shortlink.webapp.exception.MailVerificationException;
import com.shortlink.webapp.exception.ResetPasswordException;
import com.shortlink.webapp.exception.UserNotExistsException;
import com.shortlink.webapp.property.TokenProperty;
import com.shortlink.webapp.repository.ResetPasswordRepository;
import com.shortlink.webapp.repository.UserRepository;
import com.shortlink.webapp.util.EmailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class ResetPasswordService {
    private final UserRepository userRepository;
    private final MailSender mailSender;
    private final ResetPasswordRepository resetPasswordRepository;
    private final TokenProperty tokenProperty;
    private final PasswordEncoder passwordEncoder;

    public String createResetPasswordToken(User user) {
        String token = EmailUtil.getToken();
        ResetPassword resetPassword = ResetPassword.builder()
                .token(token)
                .user(user)
                .build();

        resetPasswordRepository.save(resetPassword);
        return token;
    }

    @Transactional
    public void forgotPassword(String emailOrUsername) {
        User user = userRepository.findByEmailOrUsername(emailOrUsername)
                .orElseThrow(() -> new UserNotExistsException(
                        "User with email or username %s does not exists".formatted(emailOrUsername)));

        String token = createResetPasswordToken(user);
        mailSender.sendEmail(MailType.FORGOT_PASSWORD, user, EmailUtil.FORGOT_PASSWORD_URL + token);
    }

    @Transactional
    public void resetPassword(ResetPasswordDto dto, String token) {
        String newPassword = dto.getNewPassword();
        if (!newPassword.equals(dto.getConfirmationPassword()))
            throw new InvalidPasswordException("The new password and its confirmation do not match");

        ResetPasswordWithUserProjection projection = resetPasswordRepository.findByToken(token)
                .orElseThrow(() -> new ResetPasswordException("Token %s does not exists".formatted(token)));

        if (projection.getResetAt() != null)
            throw new ResetPasswordException("Reset password token already used !");

        if (projection.getCreatedAt()
                .plus(tokenProperty.getResetPasswordExpiration(), ChronoUnit.MONTHS)//TODO change plus months
                .isBefore(Instant.now()))
            throw new ResetPasswordException("Time to reset password is expired");

        resetPasswordRepository.updateResetAtById(Instant.now(), projection.getResetId());
        userRepository.updatePasswordById(passwordEncoder.encode(newPassword), projection.getUserId());
    }
}
