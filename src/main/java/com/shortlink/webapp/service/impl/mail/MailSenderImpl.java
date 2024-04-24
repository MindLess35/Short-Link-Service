package com.shortlink.webapp.service.impl.mail;

import com.shortlink.webapp.domain.entity.user.User;
import com.shortlink.webapp.domain.enums.MailType;
import com.shortlink.webapp.service.interfaces.mail.MailSender;
import freemarker.template.Configuration;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MailSenderImpl implements MailSender {

    private final JavaMailSender mailSender;

    private final Configuration freemarkerConfig;

    @Async
    @Override
    public void sendEmail(MailType mailType, User user, String url) {
        switch (mailType) {
            case REGISTRATION -> sendRegistrationEmail(user, url);
            case MAIL_VERIFICATION -> sendVerificationEmail(user, url);
            case FORGOT_PASSWORD -> sendResetPasswordEmail(user, url);
        }

    }

    //    @SneakyThrows

    private void sendRegistrationEmail(User user, String verificationUrl) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            helper.setSubject("Thank you for registration, " + user.getUsername());
            helper.setTo(user.getEmail());

            StringWriter stringWriter = new StringWriter();
            Map<String, Object> model = new HashMap<>();

            model.put("username", user.getUsername());
            model.put("verificationUrl", verificationUrl);

            freemarkerConfig.getTemplate("registration.ftl")
                    .process(model, stringWriter);
            helper.setText(stringWriter.getBuffer().toString(), true);

            mailSender.send(mimeMessage);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
    private void sendVerificationEmail(User user, String verificationUrl) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            helper.setSubject("Verification for email " + user.getUsername());
            helper.setTo(user.getEmail());

            StringWriter stringWriter = new StringWriter();
            Map<String, Object> model = new HashMap<>();

            model.put("username", user.getUsername());
            model.put("verificationUrl", verificationUrl);

            freemarkerConfig.getTemplate("verification.ftl")
                    .process(model, stringWriter);
            helper.setText(stringWriter.getBuffer().toString(), true);

            mailSender.send(mimeMessage);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private void sendResetPasswordEmail(User user, String resetPasswordUrl) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            helper.setSubject("Reset password for " + user.getUsername());
            helper.setTo(user.getEmail());

            StringWriter stringWriter = new StringWriter();
            Map<String, Object> model = new HashMap<>();

            model.put("username", user.getUsername());
            model.put("resetPasswordUrl", resetPasswordUrl);

            freemarkerConfig.getTemplate("resetPassword.ftl")
                    .process(model, stringWriter);
            helper.setText(stringWriter.getBuffer().toString(), true);

            mailSender.send(mimeMessage);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
