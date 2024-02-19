package com.shortlink.webapp.mapper;

import com.shortlink.webapp.dto.response.LinkReadDto;
import com.shortlink.webapp.entity.Link;
import jakarta.validation.Valid;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Valid
@Setter
@Component
@ConfigurationProperties(prefix = "http")
public class LinkReadMapper {
    @URL
    private String URL;

    public LinkReadDto toDto(Link link) {
        return LinkReadDto.builder()
                .id(link.getId())
                .originalLink(link.getOriginalLink())
                .shortLink(URL + link.getShortLink())
                .build();
    }
}
