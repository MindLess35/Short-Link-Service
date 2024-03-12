package com.shortlink.webapp.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shortlink.webapp.validation.annotation.UniqueShortLink;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LinkCreateEditDto {

    @Min(1)
    private Long userId;

    @Min(1)
    private Long lifeTime;

    @URL
    @NotBlank
    private String originalLink;

    private String key;

    @UniqueShortLink
    private String customLinkName;

//    public void setShortLink(String shortLink) {
//        this.customLinkName = shortLink;
//    }
}
