package com.shortlink.webapp.service.interfaces.mail;

import com.shortlink.webapp.domain.entity.user.User;

public interface MailVerificationService {

    void verifyMail(String token);

    String createMailVerificationToken(User user);

    void sendVerificationToken(Long userId);
}
