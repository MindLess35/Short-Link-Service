package com.shortlink.webapp.mapper;

import com.shortlink.webapp.dto.response.AllUsersReadDto;
import com.shortlink.webapp.entity.User;
import org.springframework.stereotype.Component;

@Component
//@RequiredArgsConstructor
public class AllUsersReadMapper {

    public AllUsersReadDto toDto(User user) {
        return AllUsersReadDto
                .builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
