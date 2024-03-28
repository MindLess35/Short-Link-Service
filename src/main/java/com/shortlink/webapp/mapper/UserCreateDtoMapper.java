package com.shortlink.webapp.mapper;

import com.shortlink.webapp.dto.request.UserCreateDto;
import com.shortlink.webapp.entity.User;
import com.shortlink.webapp.entity.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCreateDtoMapper {
    private final PasswordEncoder passwordEncoder;

    public User toEntity(UserCreateDto userCreateDto) {
        return User.builder()
                .username(userCreateDto.getUsername())
                .email(userCreateDto.getEmail())
                .password(passwordEncoder.encode(userCreateDto.getPassword()))
                .role(Role.USER)
                .build();
    }



}
