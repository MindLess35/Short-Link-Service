package com.shortlink.webapp.dto.user.response;

import com.shortlink.webapp.validation.annotation.Password;
import com.shortlink.webapp.validation.annotation.Username;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDto {

    @Username
    @NotBlank(message = "{username.notblank}")
    private String username;

    @Password
    @NotBlank(message = "{password.notblank}")
    private String password;
}
