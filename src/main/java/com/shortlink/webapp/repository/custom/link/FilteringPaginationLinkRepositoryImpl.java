package com.shortlink.webapp.repository.custom.link;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.JPQLQuery;
import com.shortlink.webapp.dto.response.AllLinksReadDto;
import com.shortlink.webapp.exception.NoSuchOrderByFieldException;
import com.shortlink.webapp.repository.custom.FilteringPaginationBase;
import jakarta.persistence.EntityManager;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static com.shortlink.webapp.entity.QLink.link;
import static com.shortlink.webapp.entity.QLinkStatistics.linkStatistics;

public class FilteringPaginationLinkRepositoryImpl
        extends FilteringPaginationBase
        implements FilteringPaginationLinkRepository {

    private final MessageSource messageSource;

    public FilteringPaginationLinkRepositoryImpl(MessageSource messageSource, EntityManager entityManager) {
        super(entityManager);
        this.messageSource = messageSource;
    }

    @Override
    public Page<AllLinksReadDto> findPageOfLinks(Predicate predicate, Pageable pageable) {
        return getAllInPages(
                extractOrderSpecifiers(pageable),
                countTotalResult(predicate, link, link.linkStatistics),
                createProjection(),
                link,
                predicate,
                pageable,
                link.linkStatistics
        );
    }

    @Override
    public Page<AllLinksReadDto> findPageOfLinksForUser(Predicate predicate, Pageable pageable) {
        return getAllInPages(
                extractOrderSpecifiers(pageable),
                countTotalResult(predicate, link, link.user, link.linkStatistics),
                createProjection(),
                link,
                predicate,
                pageable,
                link.user, link.linkStatistics
        );
    }

    //    private PageImpl<AllLinksReadDto> getAllLinksReadDto(List<OrderSpecifier<?>> orderSpecifiers,
//                                                         Long total,
//                                                         Predicate predicate,
//                                                         Pageable pg,
//                                                         EntityPathBase<?>... joins) {
//        if (total == 0)
//            return new PageImpl<>(new ArrayList<>(), pg, total);
//
//        StringExpression dateOfCreation = Expressions.stringTemplate("TO_CHAR({0}, 'yyyy-mm-dd HH24:MI:SS')",
//                        linkStatistics.dateOfCreation)
//                .as("dateOfCreation");
//
//        StringExpression dateOfLastUses = Expressions.stringTemplate("TO_CHAR({0}, 'yyyy-mm-dd HH24:MI:SS')",
//                        linkStatistics.dateOfLastUses);
//
//        JPQLQuery<AllLinksReadDto> query = new JPAQuery<AllLinksReadDto>(entityManager)
//                .select(Projections.fields(
//                        AllLinksReadDto.class,
//                        link.id,
//                        link.originalLink,
//                        link.shortLink,
//                        dateOfCreation,
//                        dateOfLastUses.coalesce(messageSource.getMessage("link.notusedyet",
//                                null, LocaleContextHolder.getLocale()))
//                                .as("dateOfLastUses"),
//                        linkStatistics.countOfUses
//                ))
//                .from(link)
//                .where(predicate)
//                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
//                .offset(pg.getOffset())
//                .limit(pg.getPageSize());
//
//        for (EntityPathBase<?> joinTo : joins) {
//            query = query.innerJoin(joinTo);
//        }
//
//        List<AllLinksReadDto> readDtoList = query.fetch();
//        return new PageImpl<>(readDtoList, pg, total);
//    }
    @Override
    protected QBean<AllLinksReadDto> createProjection() {
        StringExpression dateOfCreation = Expressions.stringTemplate("TO_CHAR({0}, 'yyyy-mm-dd HH24:MI:SS')",
                        linkStatistics.dateOfCreation)
                .as("dateOfCreation");

        StringExpression dateOfLastUses = Expressions.stringTemplate("TO_CHAR({0}, 'yyyy-mm-dd HH24:MI:SS')",
                linkStatistics.dateOfLastUses);

        return Projections.fields(
                AllLinksReadDto.class,
                link.id,
                link.originalLink,
                link.shortLink,
                dateOfCreation,
                dateOfLastUses.coalesce(messageSource.getMessage("link.notusedyet",
                                null, LocaleContextHolder.getLocale()))
                        .as("dateOfLastUses"),
                linkStatistics.countOfUses
        );
    }


//    private List<OrderSpecifier<?>> extractOrderSpecifiers(Pageable pageable) {
//        Sort sort = pageable.getSort();
//        if (sort.isEmpty())
//            return new ArrayList<>();
//
//        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
//
//        for (Sort.Order order : sort) {
//            Expression<? extends Comparable<?>> orderBy;
//
//            orderBy = switch (order.getProperty()) {
//                case "shortLink" -> link.shortLink;
//                case "originalLink" -> link.originalLink;
//                case "dateOfCreation" -> linkStatistics.dateOfCreation;
//                case "dateOfLastUses" -> linkStatistics.dateOfLastUses;
//                case "countOfUses" -> linkStatistics.countOfUses;
//                default -> throw new NoSuchOrderByFieldException("Unsupported sort field: " + order.getProperty());
//            };
//
//            OrderSpecifier<?> orderSpecifier = new OrderSpecifier<>(
//                    order.isAscending() ? Order.ASC : Order.DESC,
//                    orderBy).nullsLast();
//
//            orderSpecifiers.add(orderSpecifier);
//        }
//
//        return orderSpecifiers;
//    }

    @Override
    protected <T> JPQLQuery<T> joiningTables(EntityPathBase<?>[] joins, JPQLQuery<T> query) {
        for (EntityPathBase<?> joinTo : joins) {
            query = query.innerJoin(joinTo);
        }
        return query;
    }

    @Override
    protected Expression<? extends Comparable<?>> getOrderBy(Sort.Order order) {
        return switch (order.getProperty()) {
            case "shortLink" -> link.shortLink;
            case "originalLink" -> link.originalLink;
            case "dateOfCreation" -> linkStatistics.dateOfCreation;
            case "dateOfLastUses" -> linkStatistics.dateOfLastUses;
            case "countOfUses" -> linkStatistics.countOfUses;
            default -> throw new NoSuchOrderByFieldException("Unsupported sort field: " + order.getProperty());
        };
    }

}
