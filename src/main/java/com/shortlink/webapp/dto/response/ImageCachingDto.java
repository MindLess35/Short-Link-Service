package com.shortlink.webapp.dto.response;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class ImageCachingDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -5384826934437499673L;
    private byte[] content;
    private String contentType;
}
