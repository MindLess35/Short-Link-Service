package com.shortlink.webapp.mapper;

import com.shortlink.webapp.dto.response.UserReadDto;
import com.shortlink.webapp.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserReadDtoMapper {
    public UserReadDto toDto(User user) {
        return UserReadDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }


}
