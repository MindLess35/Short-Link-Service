package com.shortlink.webapp.service.impl.mail;

import com.shortlink.webapp.domain.entity.user.MailVerification;
import com.shortlink.webapp.domain.entity.user.User;
import com.shortlink.webapp.domain.enums.MailType;
import com.shortlink.webapp.domain.exception.base.ResourceNotFoundException;
import com.shortlink.webapp.domain.exception.user.MailVerificationException;
import com.shortlink.webapp.domain.property.TokenProperty;
import com.shortlink.webapp.dto.projection.user.MailVerificationWithUserProjection;
import com.shortlink.webapp.repository.jpa.user.MailVerificationRepository;
import com.shortlink.webapp.repository.jpa.user.UserRepository;
import com.shortlink.webapp.service.interfaces.mail.MailSender;
import com.shortlink.webapp.service.interfaces.mail.MailVerificationService;
import com.shortlink.webapp.util.EmailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class MailVerificationServiceImpl implements MailVerificationService {

    private final MailVerificationRepository mailVerificationRepository;
    private final UserRepository userRepository;
    private final MailSender mailSender;
    private final TokenProperty tokenProperty;

    @Override
    @Transactional
    public void verifyMail(String token) {
        MailVerificationWithUserProjection verification = mailVerificationRepository
                .findVerificationWithUserByToken(token)
                .orElseThrow(() -> new MailVerificationException(
                        "Token %s does not exists".formatted(token)));

        if (verification.getVerified())
            throw new MailVerificationException("User's email already verified !");

        if (verification.getCreatedAt()
                .plus(tokenProperty.getVerifyEmailExpiration(), ChronoUnit.MONTHS) //TODO change plus months
                .isBefore(Instant.now()))
            throw new MailVerificationException("Time to verify mail is expired");

        mailVerificationRepository.updateVerifiedAtById(verification.getVerificationId(), Instant.now());

        userRepository.updateVerifiedById(verification.getUserId());
    }

    @Override
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

    @Override
    @Transactional
    public void sendVerificationToken(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(
                "User with id %s does not exists".formatted(userId)));

        if (user.getVerified())
            throw new MailVerificationException("User's email already verified !");

        String token = createMailVerificationToken(user);
        mailSender.sendEmail(MailType.MAIL_VERIFICATION, user, EmailUtil.VERIFICATION_URL + token);
    }
}
