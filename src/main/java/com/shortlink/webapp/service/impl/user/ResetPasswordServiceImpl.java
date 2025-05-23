package com.shortlink.webapp.service.impl.user;

import com.shortlink.webapp.domain.entity.user.ResetPassword;
import com.shortlink.webapp.domain.entity.user.User;
import com.shortlink.webapp.domain.enums.MailType;
import com.shortlink.webapp.domain.exception.base.ResourceNotFoundException;
import com.shortlink.webapp.domain.exception.user.password.PasswordConfirmationException;
import com.shortlink.webapp.domain.exception.user.password.ResetPasswordException;
import com.shortlink.webapp.domain.property.TokenProperty;
import com.shortlink.webapp.dto.projection.user.ResetPasswordWithUserProjection;
import com.shortlink.webapp.dto.user.response.ResetPasswordDto;
import com.shortlink.webapp.repository.jpa.user.ResetPasswordRepository;
import com.shortlink.webapp.repository.jpa.user.UserRepository;
import com.shortlink.webapp.service.interfaces.mail.MailSender;
import com.shortlink.webapp.service.interfaces.user.ResetPasswordService;
import com.shortlink.webapp.util.EmailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class ResetPasswordServiceImpl implements ResetPasswordService {
    private final UserRepository userRepository;
    private final MailSender mailSender;
    private final ResetPasswordRepository resetPasswordRepository;
    private final TokenProperty tokenProperty;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String createResetPasswordToken(User user) {
        String token = EmailUtil.getToken();
        ResetPassword resetPassword = ResetPassword.builder()
                .token(token)
                .user(user)
                .build();

        resetPasswordRepository.save(resetPassword);
        return token;
    }

    @Override
    @Transactional
    public void forgotPassword(String emailOrUsername) {
        User user = userRepository.findByEmailOrUsername(emailOrUsername)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with email or username %s does not exists".formatted(emailOrUsername)));

        String token = createResetPasswordToken(user);
        mailSender.sendEmail(MailType.FORGOT_PASSWORD, user, EmailUtil.RESET_PASSWORD_URL + token);
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordDto dto, String token) {
        String newPassword = dto.getNewPassword();
        if (!newPassword.equals(dto.getConfirmationPassword()))
            throw new PasswordConfirmationException("The new password and its confirmation do not match");

        ResetPasswordWithUserProjection projection = resetPasswordRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Token %s does not exists".formatted(token)));

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
