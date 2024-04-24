package com.shortlink.webapp.mapper.user;

import com.shortlink.webapp.dto.user.response.UserUpdateDto;
import com.shortlink.webapp.domain.entity.user.User;
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
        return user;
    }



}
