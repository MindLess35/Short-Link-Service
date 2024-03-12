package com.shortlink.webapp.repository.custom;

import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.shortlink.webapp.dto.response.AllLinksReadDto;
import com.shortlink.webapp.exception.NoSuchOrderByFieldException;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

import static com.shortlink.webapp.entity.QLink.link;
import static com.shortlink.webapp.entity.QLinkStatistics.linkStatistics;

@RequiredArgsConstructor
public abstract class FilteringPaginationBase {

    private final EntityManager entityManager;

    private PageImpl<AllLinksReadDto> getAllLinksReadDto(List<OrderSpecifier<?>> orderSpecifiers,
                                                         Long total,
                                                         Predicate predicate,
                                                         Pageable pg,
                                                         EntityPathBase<?>... joins) {
        if (total == 0)
            return new PageImpl<>(new ArrayList<>(), pg, total);

        StringExpression dateOfCreation = Expressions.stringTemplate("TO_CHAR({0}, 'yyyy-mm-dd HH24:MI:SS')",
                        linkStatistics.dateOfCreation)
                .as("dateOfCreation");

        StringExpression dateOfLastUses = Expressions.stringTemplate("TO_CHAR({0}, 'yyyy-mm-dd HH24:MI:SS')",
                linkStatistics.dateOfLastUses);

        JPQLQuery<AllLinksReadDto> query = new JPAQuery<AllLinksReadDto>(entityManager)
                .select(Projections.fields(
                        AllLinksReadDto.class,
                        link.id,
                        link.originalLink,
                        link.shortLink,git config --global user.email "you@example.com"
                        git config --global user.name "Your Name"
                        dateOfCreation,
//                        dateOfLastUses.coalesce(messageSource.getMessage("link.notusedyet",
//                                        null, LocaleContextHolder.getLocale()))
//                                .as("dateOfLastUses"),
                        linkStatistics.countOfUses
                ))
                .from(link)
                .where(predicate)
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .offset(pg.getOffset())
                .limit(pg.getPageSize());

        for (EntityPathBase<?> joinTo : joins) {
            query = query.innerJoin(joinTo);
        }

        List<AllLinksReadDto> readDtoList = query.fetch();
        return new PageImpl<>(readDtoList, pg, total);
    }

    private List<OrderSpecifier<?>> extractOrderSpecifiers(Pageable pageable) {
        Sort sort = pageable.getSort();
        if (sort.isEmpty())
            return new ArrayList<>();

        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        for (Sort.Order order : sort) {
            Expression<? extends Comparable<?>> orderBy;

            orderBy = switch (order.getProperty()) {
                case "shortLink" -> link.shortLink;
                case "originalLink" -> link.originalLink;
                case "dateOfCreation" -> linkStatistics.dateOfCreation;
                case "dateOfLastUses" -> linkStatistics.dateOfLastUses;
                case "countOfUses" -> linkStatistics.countOfUses;
                default -> throw new NoSuchOrderByFieldException("Unsupported sort field: " + order.getProperty());
            };

            OrderSpecifier<?> orderSpecifier = new OrderSpecifier<>(
                    order.isAscending() ? Order.ASC : Order.DESC,
                    orderBy).nullsLast();

            orderSpecifiers.add(orderSpecifier);
        }

        return orderSpecifiers;
    }

    private Long countTotalResult(Predicate predicate, EntityPathBase<?>... joins) {
        JPQLQuery<Long> query = new JPAQuery<>(entityManager)
                .select(link.id.count())
                .from(link)
                .where(predicate);

        for (EntityPathBase<?> joinTo : joins) {
            query = query.innerJoin(joinTo);
        }

        return query.fetchFirst();
    }
}
