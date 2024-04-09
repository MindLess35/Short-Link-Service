package com.shortlink.webapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserReadDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 7014305132647108132L;

    private Long id;

    private String username;

    private String email;
}
