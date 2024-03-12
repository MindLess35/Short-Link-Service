package com.shortlink.webapp.repository;

import com.shortlink.webapp.dto.LinkCheckKeyDto;
import com.shortlink.webapp.entity.Link;
import com.shortlink.webapp.entity.User;
import com.shortlink.webapp.repository.custom.FilteringPaginationLinkRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
//@Profile
public interface LinkRepository extends
        JpaRepository<Link, Long>,
//        QuerydslPredicateExecutor<Link>,
        FilteringPaginationLinkRepository {
//    @Query(value = """
//            SELECT l.id, l.originalLink, l.shortLink
//            FROM LinkStatistics ls
//            INNER JOIN ls.Link l
//
//            """)
//    Slice<LinkReadDto> findAllByPageable(Pageable pageable);

    //    @Query(value = """
//            SELECT l.id, l.original_link, l.short_link, l.user_id, l.encrypted_key
//            FROM link_statistics ls
//            INNER JOIN link l ON ls.link_id = l.id
//            """, nativeQuery = true)
//    Slice<Link> findAllByPageable2(Pageable pageable);
//    INNER JOIN l.linkStatistics
//    @Query(value = """
//            SELECT new com.shortlink.webapp.dto.response.LinkReadDto(l.id, l.originalLink, l.shortLink)
//            FROM Link l
//            """)
//    Slice<LinkReadDto> findAllByPageable(Pageable pageable);

//    @Query("SELECT new com.shortlink.webapp.dto.response.LinkReadDto(l.id, l.originalLink, l.shortLink)" +
//           " FROM Link l " +
//           "JOIN l.linkStatistics ls")
//    Slice<LinkReadDto> findAllByPageable(Pageable pageable);
//}

//    @Query(value = """
//            SELECT COUNT(short_link_name) = 1
//            FROM link
//            WHERE short_link_name = :shortLink
//            """, nativeQuery = true)
//    boolean shortLinkNameAlreadyExists(@Param("shortLink") String shortLink);

    boolean existsByShortLink(String shortLink);
//    @Query(value = """
//            SELECT original_link
//            FROM link
//            WHERE short_link_name = :shortLink
//            """, nativeQuery = true)

//    Optional<String> getOriginalLinkByShortName(@Param("shortLink") String shortLink);
    //new LinkCheckKeyDto(

    @Query(value = """
            SELECT originalLink, key
            FROM Link
            WHERE shortLink = :shortLink
            """)
    Optional<LinkCheckKeyDto> findByShortLinkWithKey(@Param("shortLink") String shortLink);

    Optional<Link> findByShortLink(String shortLink);

    //    @Query(value = """
//            SELECT originalLink
//            FROM Link
//            WHERE shortLink = :shortLink
//            """)
//    Optional<String> findOriginalLinkByShortLink(@Param("shortLink") String shortLink);

//    List<Link> findAllByUser(User user);

    //    @BatchSize(size = 50)
    @Modifying
    @Query(value = """
            DELETE FROM Link l
            WHERE l.user = :user
            """)
    void deleteAllByUser(User user);//regexp_replace


    boolean existsByIdAndUser(Long requestLinkId, User authorizedUser);
}
