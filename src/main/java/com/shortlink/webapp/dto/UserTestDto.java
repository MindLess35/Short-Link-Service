package com.shortlink.webapp.dto;

import com.shortlink.webapp.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserTestDto {

    private String username;

    private String email;


    private String password;
    private Role role;
}
