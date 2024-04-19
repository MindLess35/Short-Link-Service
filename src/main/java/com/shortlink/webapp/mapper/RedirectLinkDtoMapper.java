package com.shortlink.webapp.mapper;

import com.shortlink.webapp.dto.response.LinkReadDto;
import com.shortlink.webapp.dto.response.RedirectLinkDto;
import com.shortlink.webapp.entity.Link;
import org.springframework.stereotype.Component;

@Component
public class RedirectLinkDtoMapper {
    public RedirectLinkDto toDto(Link link) {
        return RedirectLinkDto.builder()
                .id(link.getId())
                .originalLink(link.getOriginalLink())
                .shortLink(link.getShortLink())
                .key(link.getKey())
                .build();
    }
}
