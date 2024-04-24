package com.shortlink.webapp.mapper.user;

import com.shortlink.webapp.dto.user.response.AllUsersReadDto;
import com.shortlink.webapp.domain.entity.user.User;
import org.springframework.stereotype.Component;

@Component
//@RequiredArgsConstructor
public class AllUsersReadDtoMapper { //todo remove this mapper

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
