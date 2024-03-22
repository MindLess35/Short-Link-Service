package com.shortlink.webapp.service;

import com.shortlink.webapp.dto.projection.MailVerificationWithUserProjection;
import com.shortlink.webapp.entity.MailVerification;
import com.shortlink.webapp.entity.User;
import com.shortlink.webapp.entity.enums.MailType;
import com.shortlink.webapp.exception.MailVerificationException;
import com.shortlink.webapp.exception.UserNotExistsException;
import com.shortlink.webapp.property.TokenProperty;
import com.shortlink.webapp.repository.MailVerificationRepository;
import com.shortlink.webapp.repository.UserRepository;
import com.shortlink.webapp.util.EmailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MailVerificationService {

    private final MailVerificationRepository mailVerificationRepository;
    private final UserRepository userRepository;
    private final MailSender mailSender;
    private final TokenProperty tokenProperty;


    @Transactional
    public void verifyMail(String token) {
        MailVerificationWithUserProjection verification = mailVerificationRepository
                .findVerificationWithUserByToken(token)
                .orElseThrow(() -> new MailVerificationException(
                        "Token %s does not exists".formatted(token)));

        if (verification.getVerified())
            throw new MailVerificationException("User's email already verified !");

        if (verification.getCreatedAt()
                .plusMonths(tokenProperty.getVerifyEmailExpiration()) //TODO change plus months
                .isBefore(LocalDateTime.now()))
            throw new MailVerificationException("Time to verify mail is expired");

        mailVerificationRepository.updateVerifiedAtById(verification.getVerificationId(), LocalDateTime.now());

        userRepository.updateVerifiedById(verification.getUserId());
    }

    @Transactional
    public String createMailVerificationToken(User user) {
        String token = EmailUtil.getToken();
        MailVerification mailVerification = MailVerification.builder()
                .token(token)
                .user(user)
                .build();

        mailVerificationRepository.save(mailVerification);
        return token;
    }

    @Transactional
    public void sendVerificationToken(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotExistsException(
                "User with id %s does not exists".formatted(userId)));

        if (user.getVerified())
            throw new MailVerificationException("User's email already verified !");

        String token = createMailVerificationToken(user);
        mailSender.sendEmail(MailType.MAIL_VERIFICATION, user, EmailUtil.VERIFICATION_URL + token);
    }
}
