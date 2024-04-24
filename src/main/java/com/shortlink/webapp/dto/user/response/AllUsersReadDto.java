package com.shortlink.webapp.dto.user.response;

import com.shortlink.webapp.domain.enums.Role;
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

//    private Boolean isEmailVerify;//todo add isEmailVerify

//    private Boolean active;

}
