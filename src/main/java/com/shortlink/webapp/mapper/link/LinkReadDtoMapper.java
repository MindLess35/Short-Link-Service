package com.shortlink.webapp.mapper.link;

import com.shortlink.webapp.dto.projection.link.LinkWithTtlProjection;
import com.shortlink.webapp.dto.link.response.LinkReadDto;
import com.shortlink.webapp.domain.entity.link.Link;
import org.springframework.stereotype.Component;

@Component
public class LinkReadDtoMapper {

    public LinkReadDto toDto(Link link) {
        return LinkReadDto.builder()
                .id(link.getId())
                .originalLink(link.getOriginalLink())
                .shortLink(link.getShortLink())
//                .userId(link.getUserId())
                .build();
    }

    public LinkReadDto toDto(LinkWithTtlProjection link) {
        return LinkReadDto.builder()
                .id(link.getId())
                .originalLink(link.getOriginalLink())
                .shortLink(link.getShortLink())
//                .userId(link.getUserId())
                .build();
    }
}
