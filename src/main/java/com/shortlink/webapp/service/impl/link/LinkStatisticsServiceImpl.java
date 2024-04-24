package com.shortlink.webapp.service.impl.link;

import com.shortlink.webapp.domain.entity.link.Link;
import com.shortlink.webapp.domain.entity.link.LinkStatistics;
import com.shortlink.webapp.dto.projection.link.TopLinkSourceSitesProjection;
import com.shortlink.webapp.repository.jpa.link.LinkRepository;
import com.shortlink.webapp.repository.jpa.link.LinkStatisticsRepository;
import com.shortlink.webapp.service.interfaces.link.LinkStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LinkStatisticsServiceImpl implements LinkStatisticsService {
    private final LinkStatisticsRepository linkStatisticsRepository;
    private final LinkRepository linkRepository;

    @Override
    public void createLinkStatistics(Link link, Instant timeToLive) {
        linkStatisticsRepository.save(
                LinkStatistics.builder()
                        .link(link)
                        .timeToLive(timeToLive)
                        .build()
        );
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean isTimeToLiveExpired(LinkStatistics linkStatistics) {
        Instant timeToLive = linkStatistics.getTimeToLive();
        if (timeToLive == null)
            return false;

        if (Instant.now()
                .isAfter(timeToLive)) {
            linkRepository.delete(linkStatistics.getLink());
            return true;
        }
        return false;
    }

    @Override
    public List<TopLinkSourceSitesProjection> getTopLinkSourceSites(Short top) {
        return linkStatisticsRepository.getTopLinkSourceSites(top);
    }

    @Override
    public Long getCountAllClicks() {
        return linkStatisticsRepository.sumByCountOfUses();
    }

    @Override
    public void changeTtl(Long linkId, Instant ttl) {
        linkStatisticsRepository.changeTtlByLinkId(linkId, ttl);
    }
}
