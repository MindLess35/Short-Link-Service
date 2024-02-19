package com.shortlink.webapp.dto.request;

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
public class UserCreateEditDto {

    @Username
    @NotBlank
    private String username;

//    @Email
    @NotBlank
    private String email;


    @NotBlank
    private String password;
}
