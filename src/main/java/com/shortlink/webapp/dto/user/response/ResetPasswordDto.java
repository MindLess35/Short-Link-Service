package com.shortlink.webapp.dto.user.response;

import com.shortlink.webapp.validation.annotation.Password;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Password
@NoArgsConstructor
@AllArgsConstructor
@NotBlank(message = "{password.notblank}")
public class ResetPasswordDto {

    private String newPassword;

    private String confirmationPassword;

}
