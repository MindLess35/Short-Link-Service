package com.shortlink.webapp.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AllUsersReadDto {

    private Long id;

    private String username;

    private String email;

    private String role;

//    private Boolean isEmailVerify;

//    private Boolean active;

}
