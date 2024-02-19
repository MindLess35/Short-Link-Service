package com.shortlink.webapp.mapper;

import com.shortlink.webapp.dto.request.UserCreateEditDto;
import com.shortlink.webapp.dto.response.UserReadDto;
import com.shortlink.webapp.entity.User;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class UserReadDtoMapper implements Function<User, UserReadDto> {
    @Override
    public UserReadDto apply(User user) {
        return UserReadDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }


}
