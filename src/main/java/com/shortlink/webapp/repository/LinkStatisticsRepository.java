package com.shortlink.webapp.repository;

import com.shortlink.webapp.dto.projection.TopLinkSourceSitesProjection;
import com.shortlink.webapp.entity.Link;
import com.shortlink.webapp.entity.LinkStatistics;
import jakarta.persistence.QueryHint;
import org.springframework.boot.autoconfigure.batch.BatchDataSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LinkStatisticsRepository extends JpaRepository<LinkStatistics, Long> {
    Optional<LinkStatistics> findByLink(Link link);
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

}
