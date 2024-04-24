package com.shortlink.webapp.mapper.user;

import com.shortlink.webapp.dto.user.response.UserReadDto;
import com.shortlink.webapp.domain.entity.user.User;
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
