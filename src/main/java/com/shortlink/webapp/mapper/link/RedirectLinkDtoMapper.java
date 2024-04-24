package com.shortlink.webapp.mapper.link;

import com.shortlink.webapp.dto.link.response.RedirectLinkDto;
import com.shortlink.webapp.domain.entity.link.Link;
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
