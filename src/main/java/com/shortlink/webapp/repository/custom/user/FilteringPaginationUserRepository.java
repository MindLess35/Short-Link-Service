package com.shortlink.webapp.repository.custom.user;

import com.querydsl.core.types.Predicate;
import com.shortlink.webapp.dto.response.AllLinksReadDto;
import com.shortlink.webapp.dto.response.AllUsersReadDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FilteringPaginationUserRepository {
    Page<AllUsersReadDto> findPageOfUsers(Predicate predicate, Pageable pageable);

}
