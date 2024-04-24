package com.shortlink.webapp.mapper.link;

import com.shortlink.webapp.dto.link.request.LinkUpdateDto;
import com.shortlink.webapp.domain.entity.link.Link;
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
