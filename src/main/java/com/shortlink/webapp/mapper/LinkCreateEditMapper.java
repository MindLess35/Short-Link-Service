package com.shortlink.webapp.mapper;

import com.shortlink.webapp.dto.request.LinkCreateEditDto;
import com.shortlink.webapp.entity.Link;
import org.springframework.stereotype.Component;

@Component
public class LinkCreateEditMapper {//implements Function<UserCreateDto, User>

    public Link toEntity(LinkCreateEditDto dto) {
        return Link.builder()
                .originalLink(dto.getOriginalLink())
                .shortLink(dto.getCustomLinkName())
                .key(dto.getKey())
                .build();
    }

    public Link updateEntity(Link link, LinkCreateEditDto dto) {
//        return Link.builder()
//                .user(link.getUser())
//                .originalLink(dto.getOriginalLink())
//                .shortLinkName(dto.getCustomLinkName())
//                .linkStatistics(link.getLinkStatistics())//TODO
//                .encryptedKey(dto.getKey())
//                .build();
        link.setShortLink(dto.getCustomLinkName());
        link.setOriginalLink(dto.getOriginalLink());
        link.setKey(dto.getKey());

        return link;

    }
}
