package com.shortlink.webapp.mapper;

import com.shortlink.webapp.dto.projection.LinkWithTtlProjection;
import com.shortlink.webapp.dto.request.LinkUpdateDto;
import com.shortlink.webapp.entity.Link;
import org.springframework.stereotype.Component;

@Component
public class LinkUpdateDtoMapper {

    public Link updateEntity(Long linkId, LinkUpdateDto dto) {
       return Link.builder()
                .id(linkId)
                .originalLink(dto.getOriginalLink())
                .shortLink(dto.getCustomLinkName())
                .key(dto.getKey())
                .build();
    }
    public Link updateEntity(Link link, LinkUpdateDto dto) {
        link.setShortLink(dto.getCustomLinkName());
        link.setOriginalLink(dto.getOriginalLink());
        link.setKey(dto.getKey());

        return link;
    }

}
