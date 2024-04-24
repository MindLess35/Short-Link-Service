package com.shortlink.webapp.repository.jpa.link;

import com.shortlink.webapp.domain.entity.link.Link;
import com.shortlink.webapp.domain.entity.user.User;
import com.shortlink.webapp.dto.projection.link.LinkWithTtlProjection;
import com.shortlink.webapp.repository.jpa.custom.link.FilteringPaginationLinkRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Stream;

@Repository
//@Profile
public interface LinkRepository extends
        JpaRepository<Link, Long>,
        FilteringPaginationLinkRepository {

    boolean existsByShortLink(String shortLink);


    Optional<Link> findByShortLink(String shortLink);

    @Modifying
    @Query(value = """
            DELETE FROM link l
            WHERE l.user_id = :userId
            RETURNING l.id
            """, nativeQuery = true)
    Stream<Long> deleteAllByUserId(Long userId);

    boolean existsByIdAndUser(Long requestLinkId, User authorizedUser);

    @Query("""
            SELECT l.id AS id,
                   l.key AS key,
                   l.originalLink AS originalLink,
                   l.shortLink AS shortLink,
                   ls.timeToLive AS ttl
            FROM Link l
            INNER JOIN l.linkStatistics ls
            WHERE l.id = :id
            """)
    Optional<LinkWithTtlProjection> findLinkWithTtlById(Long id);

    @Query("""
            SELECT l.id AS id,
                   l.key AS key,
                   l.originalLink AS originalLink,
                   l.shortLink AS shortLink,
                   ls.timeToLive AS ttl
            FROM Link l
            INNER JOIN l.linkStatistics ls
            WHERE l.shortLink = :shortLink
            """)
    Optional<LinkWithTtlProjection> findLinkWithTtlByShortLink(String shortLink);

}
