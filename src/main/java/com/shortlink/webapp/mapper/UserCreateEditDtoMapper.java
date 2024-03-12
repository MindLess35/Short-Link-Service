package com.shortlink.webapp.mapper;

import com.shortlink.webapp.dto.request.UserCreateEditDto;
import com.shortlink.webapp.entity.User;
import com.shortlink.webapp.entity.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class UserCreateEditDtoMapper implements Function<UserCreateEditDto, User> {
    private final PasswordEncoder passwordEncoder;

    @Override
    public User apply(UserCreateEditDto userCreateEditDto) {
        return User.builder()
                .username(userCreateEditDto.getUsername())
                .email(userCreateEditDto.getEmail())
                .password(passwordEncoder.encode(userCreateEditDto.getPassword()))
                .role(Role.USER)
                .build();
    }


    public User updateEntity(UserCreateEditDto userCreateEditDto, User user) {
        user.setUsername(userCreateEditDto.getUsername());
        user.setEmail(userCreateEditDto.getEmail());
        user.setPassword(passwordEncoder.encode(userCreateEditDto.getPassword()));
        return user;
    }
}
