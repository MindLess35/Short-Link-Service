package com.shortlink.webapp.service.interfaces.user;

import com.shortlink.webapp.domain.entity.user.User;
import com.shortlink.webapp.dto.user.response.ResetPasswordDto;
public interface ResetPasswordService {

    String createResetPasswordToken(User user);

    void forgotPassword(String emailOrUsername);

    void resetPassword(ResetPasswordDto dto, String token);
}


