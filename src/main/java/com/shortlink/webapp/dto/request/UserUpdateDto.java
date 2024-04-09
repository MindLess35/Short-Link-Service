package com.shortlink.webapp.dto.request;

import com.shortlink.webapp.validation.annotation.UniqueEmail;
import com.shortlink.webapp.validation.annotation.Username;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDto {

    @Username
    @NotBlank(message = "{username.notblank}")
    private String username;

    @UniqueEmail
    @Email(message = "{email.valid}")
    @NotBlank(message = "{email.notblank}")
    private String email;

}
