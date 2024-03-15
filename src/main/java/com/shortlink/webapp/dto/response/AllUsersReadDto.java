package com.shortlink.webapp.dto.response;

import com.shortlink.webapp.entity.enums.Role;
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

    private Role role;

//    private Boolean isEmailVerify;

//    private Boolean active;

}
