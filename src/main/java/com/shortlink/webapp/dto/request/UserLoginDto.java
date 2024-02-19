package com.shortlink.webapp.dto.request;

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

    @NotBlank
    @Username
    private String username;

    @NotBlank
    private String password;
}
