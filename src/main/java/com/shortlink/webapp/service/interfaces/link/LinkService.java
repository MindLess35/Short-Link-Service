package com.shortlink.webapp.service.interfaces.link;

import com.shortlink.webapp.dto.link.request.AllLinksReadDto;
import com.shortlink.webapp.dto.link.request.LinkCreateDto;
import com.shortlink.webapp.dto.link.request.LinkUpdateDto;
import com.shortlink.webapp.dto.link.response.LinkReadDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.Map;

public interface LinkService {

    LinkReadDto getLinkById(Long id);

    LinkReadDto createLink(LinkCreateDto dto);

    void deleteAllUsersLinks(Long userId);
    void deleteLink(Long id);
    LinkReadDto changeLink(Long id, Map<String, Object> fields);
    LinkReadDto updateLink(Long id, LinkUpdateDto dto);
    Page<AllLinksReadDto> getAllUsersLinksByPageableAndFilter(Long id,
                                                              Pageable pageable,
                                                              String shortLink,
                                                              String originalLink,
                                                              Instant dateOfCreation,
                                                              Instant dateOfLastUses,
                                                              Long countOfUses,
                                                              Instant timeToLive);
    Page<AllLinksReadDto> findAllLinksByPageableAndFilter(Pageable pageable,
                                                          String shortLink,
                                                          String originalLink,
                                                          Instant dateOfCreation,
                                                          Instant dateOfLastUses,
                                                          Long countOfUses,
                                                          Boolean isUserExists,
                                                          Instant timeToLive);
}
