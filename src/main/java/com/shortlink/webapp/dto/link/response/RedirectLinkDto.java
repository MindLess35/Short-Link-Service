package com.shortlink.webapp.dto.link.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Builder
//@AllArgsConstructor
public class RedirectLinkDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 5325335363494135303L;

    private Long id;

    @JsonIgnore
    private String originalLink;

    private String shortLink;

    @JsonIgnore
    private String key;

    public boolean isKeyExists() {
        return key != null;
    }

    public boolean isKeyCorrect(String key) {
        return isKeyExists() && this.key.equals(key);
    }
}
