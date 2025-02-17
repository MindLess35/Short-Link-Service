package com.shortlink.webapp.dto.link.request;

import com.shortlink.webapp.validation.annotation.UniqueShortLink;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LinkUpdateDto {

    @Future
    private Instant timeToLive;

    @URL
    @NotBlank
    private String originalLink;

    private String key;

    @UniqueShortLink
    private String customLinkName;

}
