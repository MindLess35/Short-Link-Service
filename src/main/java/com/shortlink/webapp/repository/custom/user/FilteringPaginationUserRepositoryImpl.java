package com.shortlink.webapp.repository.custom.user;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.JPQLQuery;
import com.shortlink.webapp.dto.response.AllUsersReadDto;
import com.shortlink.webapp.exception.link.NoSuchOrderByFieldException;
import com.shortlink.webapp.repository.custom.FilteringPaginationBase;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static com.shortlink.webapp.entity.QUser.user;

public class FilteringPaginationUserRepositoryImpl
        extends FilteringPaginationBase
        implements FilteringPaginationUserRepository {
    public FilteringPaginationUserRepositoryImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public Page<AllUsersReadDto> findPageOfUsers(Predicate predicate, Pageable pageable) {
        return getAllInPages(
                extractOrderSpecifiers(pageable),
                countTotalResult(predicate, user),
                createProjection(),
                user,
                predicate,
                pageable
        );
    }

    @Override
    protected QBean<AllUsersReadDto> createProjection() {
        return Projections.fields(
                AllUsersReadDto.class,
                user.id,
                user.username,
                user.email,
                user.role
        );
    }

    @Override
    protected <T> JPQLQuery<T> joiningTables(EntityPathBase<?>[] joins, JPQLQuery<T> query) {
        return query;
    }

    @Override
    protected Expression<? extends Comparable<?>> getOrderBy(Sort.Order order) {
        return switch (order.getProperty()) {
            case "username" -> user.username;
            case "email" -> user.email;
            case "role" -> user.role;
            default -> throw new NoSuchOrderByFieldException(
                    "Unsupported sort field: " + order.getProperty());
        };
    }
}
