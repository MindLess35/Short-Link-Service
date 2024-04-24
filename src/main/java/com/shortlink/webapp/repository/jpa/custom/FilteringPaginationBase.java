package com.shortlink.webapp.repository.jpa.custom;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public abstract class FilteringPaginationBase {

    private final EntityManager entityManager;

    protected <T> PageImpl<T> getAllInPages(OrderSpecifier<?>[] orderSpecifiers,
                                            Long total,
                                            QBean<T> projection,
                                            EntityPathBase<?> from,
                                            Predicate predicate,
                                            Pageable pg,
                                            EntityPathBase<?>... joins) {
        if (total == 0)
            return new PageImpl<>(new ArrayList<>(), pg, total);

        JPQLQuery<T> query = new JPAQuery<T>(entityManager)
                .select(projection)
                .from(from)
                .where(predicate)
                .orderBy(orderSpecifiers)
                .offset(pg.getOffset())
                .limit(pg.getPageSize());

        if (joins != null)
            query = joiningTables(joins, query);

        List<T> resultList = query.fetch();
        return new PageImpl<>(resultList, pg, total);
    }

    protected OrderSpecifier<?>[] extractOrderSpecifiers(Pageable pageable) {
        Sort sort = pageable.getSort();
        if (sort.isEmpty())
            return new OrderSpecifier<?>[0];

        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        for (Sort.Order order : sort) {
            Expression<? extends Comparable<?>> orderBy;
            orderBy = getOrderBy(order);

            OrderSpecifier<?> orderSpecifier = new OrderSpecifier<>(
                    order.isAscending()
                            ? Order.ASC
                            : Order.DESC,
                    orderBy).nullsLast();

            orderSpecifiers.add(orderSpecifier);
        }

        return orderSpecifiers.toArray(new OrderSpecifier[0]);
    }

    protected Long countTotalResult(Predicate predicate,
                                    EntityPathBase<?> from,
                                    EntityPathBase<?>... joins) {

        JPQLQuery<Long> query = new JPAQuery<>(entityManager)
                .select(from.count())
                .from(from)
                .where(predicate);

        if (joins != null)
            query = joiningTables(joins, query);

        return query.fetchFirst();
    }

    protected abstract <T> JPQLQuery<T> joiningTables(EntityPathBase<?>[] joins, JPQLQuery<T> query);

    protected abstract Expression<? extends Comparable<?>> getOrderBy(Sort.Order order);

    protected abstract <T> QBean<T> createProjection();
}
