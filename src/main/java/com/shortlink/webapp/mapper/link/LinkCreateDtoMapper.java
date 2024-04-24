package com.shortlink.webapp.mapper.link;

import com.shortlink.webapp.dto.link.request.LinkCreateDto;
import com.shortlink.webapp.domain.entity.link.Link;
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
