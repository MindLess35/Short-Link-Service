package com.shortlink.webapp.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserReadDto {

    private Long id;

    private String username;

    private String email;
}
