package com.shortlink.webapp.mapper;

import com.shortlink.webapp.dto.request.LinkCreateDto;
import com.shortlink.webapp.entity.Link;
import org.springframework.stereotype.Component;

@Component
public class LinkCreateDtoMapper {

    public Link toEntity(LinkCreateDto dto) {
        return Link.builder()
                .originalLink(dto.getOriginalLink())
                .shortLink(dto.getCustomLinkName())
                .key(dto.getKey())
                .build();
    }

}
