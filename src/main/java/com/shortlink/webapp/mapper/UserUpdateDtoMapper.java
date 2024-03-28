package com.shortlink.webapp.mapper;

import com.shortlink.webapp.dto.request.UserCreateDto;
import com.shortlink.webapp.dto.request.UserUpdateDto;
import com.shortlink.webapp.entity.User;
import com.shortlink.webapp.entity.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserUpdateDtoMapper {
    private final PasswordEncoder passwordEncoder;

    public User updateEntity(UserUpdateDto userUpdateDto, User user) {
        user.setUsername(userUpdateDto.getUsername());
        user.setEmail(userUpdateDto.getEmail());
        user.setPassword(passwordEncoder.encode(userUpdateDto.getPassword()));
        return user;
    }



}
