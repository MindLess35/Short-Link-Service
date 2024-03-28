package com.shortlink.webapp.mapper;

import com.shortlink.webapp.dto.request.LinkCreateDto;
import com.shortlink.webapp.dto.request.LinkUpdateDto;
import com.shortlink.webapp.entity.Link;
import org.springframework.stereotype.Component;

@Component
public class LinkUpdateDtoMapper {

    public Link updateEntity(Link link, LinkUpdateDto dto) {
        link.setShortLink(dto.getCustomLinkName());
        link.setOriginalLink(dto.getOriginalLink());
        link.setKey(dto.getKey());

        return link;

    }

}
