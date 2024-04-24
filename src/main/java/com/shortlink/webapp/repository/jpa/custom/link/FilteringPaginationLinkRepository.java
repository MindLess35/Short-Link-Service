package com.shortlink.webapp.repository.jpa.custom.link;

import com.querydsl.core.types.Predicate;
import com.shortlink.webapp.dto.link.request.AllLinksReadDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FilteringPaginationLinkRepository {
    Page<AllLinksReadDto> findPageOfLinks(Predicate predicate, Pageable pageable);

    Page<AllLinksReadDto> findPageOfLinksForUser(Predicate predicate, Pageable pageable);

//    Long countTotalNumberPages(Predicate predicate, Pageable pageable);

}
