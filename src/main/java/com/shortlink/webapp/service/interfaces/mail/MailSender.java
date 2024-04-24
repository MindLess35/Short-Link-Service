package com.shortlink.webapp.service.interfaces.mail;

import com.shortlink.webapp.domain.entity.user.User;
import com.shortlink.webapp.domain.enums.MailType;

public interface MailSender {
    void sendEmail(MailType mailType, User user, String url);

}
