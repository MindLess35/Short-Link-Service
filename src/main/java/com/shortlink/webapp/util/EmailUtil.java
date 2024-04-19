package com.shortlink.webapp.util;

import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class EmailUtil {
    public final String VERIFICATION_URL = LinkUtil.APPLICATION_URL + "api/v1/email/verify-mail?token=";
    public final String RESET_PASSWORD_URL = LinkUtil.APPLICATION_URL + "api/v1/users/reset-password?token=";

    public String getToken(){
        return UUID.randomUUID().toString();
    }
}


