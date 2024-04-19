package com.shortlink.webapp.dto.response;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class LinkReadDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -2736798644982670163L;

    private Long id;

    private String originalLink;

    private String shortLink;

    //    private Long userId;//todo add ttl here
}
