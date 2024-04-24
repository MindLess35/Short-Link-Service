package com.shortlink.webapp.service.interfaces.link;

import com.shortlink.webapp.domain.entity.link.Link;
import com.shortlink.webapp.domain.entity.link.LinkStatistics;
import com.shortlink.webapp.dto.projection.link.TopLinkSourceSitesProjection;

import java.time.Instant;
import java.util.List;

public interface LinkStatisticsService {
    void createLinkStatistics(Link link, Instant timeToLive);

    boolean isTimeToLiveExpired(LinkStatistics linkStatistics);

    List<TopLinkSourceSitesProjection> getTopLinkSourceSites(Short top);

    Long getCountAllClicks();

    void changeTtl(Long linkId, Instant ttl);
}
