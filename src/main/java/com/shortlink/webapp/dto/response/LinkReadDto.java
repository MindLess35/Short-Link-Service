package com.shortlink.webapp.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
//@AllArgsConstructor
public class LinkReadDto {
    private Long id;
    private String originalLink;
    private String shortLink;

    public LinkReadDto(Long id, String originalLink, String shortLink) {
        this.id = id;
        this.originalLink = originalLink;
        this.shortLink = shortLink;
    }
    //    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
}
