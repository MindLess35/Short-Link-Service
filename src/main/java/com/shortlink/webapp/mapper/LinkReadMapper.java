package com.shortlink.webapp.mapper;

import com.shortlink.webapp.dto.response.LinkReadDto;
import com.shortlink.webapp.entity.Link;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
public class LinkReadMapper {

    public LinkReadDto toDto(Link link) {
        return LinkReadDto.builder()
                .id(link.getId())
                .originalLink(link.getOriginalLink())
                .shortLink(link.getShortLink())
                .build();
    }
}
