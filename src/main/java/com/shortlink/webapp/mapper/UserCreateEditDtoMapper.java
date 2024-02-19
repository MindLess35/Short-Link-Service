package com.shortlink.webapp.mapper;

import com.shortlink.webapp.dto.request.UserCreateEditDto;
import com.shortlink.webapp.entity.enums.Role;
import com.shortlink.webapp.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class UserCreateEditDtoMapper implements Function<UserCreateEditDto, User> {
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

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
        user.setUsername(userCreateEditDto.getUsername());//TODO
        user.setEmail(userCreateEditDto.getEmail());
        user.setPassword(passwordEncoder.encode(userCreateEditDto.getPassword()));
        return user;
    }
}
