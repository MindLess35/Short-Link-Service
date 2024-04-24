package com.shortlink.webapp.mapper.user;

import com.shortlink.webapp.dto.user.response.UserCreateDto;
import com.shortlink.webapp.domain.entity.user.User;
import com.shortlink.webapp.domain.enums.Role;
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
