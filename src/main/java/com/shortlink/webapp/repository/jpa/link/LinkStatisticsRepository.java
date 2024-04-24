package com.shortlink.webapp.repository.jpa.link;

import com.shortlink.webapp.dto.projection.link.TopLinkSourceSitesProjection;
import com.shortlink.webapp.domain.entity.link.LinkStatistics;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface LinkStatisticsRepository extends JpaRepository<LinkStatistics, Long> {
    Optional<LinkStatistics> findByLinkId(Long linkId);

    @Query(value = """
            SELECT REGEXP_REPLACE(l.originalLink, '^(https?://[^/?#]+).*$', '\\1') site,
                   SUM(ls.countOfUses) countOfClicks
            FROM Link l
            JOIN l.linkStatistics ls
            GROUP BY site
            ORDER BY countOfClicks DESC
            LIMIT :top
            """)
    List<TopLinkSourceSitesProjection> getTopLinkSourceSites(@Param("top") Short top);

    @Query(value = """
            SELECT SUM(ls.countOfUses)
            FROM LinkStatistics ls
            """)
    Long sumByCountOfUses();

    @Modifying
    @Query("""
            UPDATE LinkStatistics ls
            SET ls.timeToLive = :ttl
            WHERE ls.link.id = :linkId
            """)
    void changeTtlByLinkId(Long linkId, Instant ttl);

}
