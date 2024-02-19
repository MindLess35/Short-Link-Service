package com.shortlink.webapp.service;

import com.shortlink.webapp.entity.Link;
import com.shortlink.webapp.entity.LinkStatistics;
import com.shortlink.webapp.exception.LifeTimeExpiredException;
import com.shortlink.webapp.exception.LinkStatisticsNotExistsException;
import com.shortlink.webapp.repository.LinkRepository;
import com.shortlink.webapp.repository.LinkStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LinkStatisticsService {
    private final LinkStatisticsRepository linkStatisticsRepository;
    private final LinkRepository linkRepository;

    //    link_id             BIGINT      NOT NULL REFERENCES link(id), +
    //    date_of_creation    TIMESTAMP   NOT NULL, +
    //    date_of_last_uses   TIMESTAMP   NOT NULL, +
    //    life_time           BIGINT      NOT NULL, +
    //    count_of_uses       BIGINT      NOT NULL  +
    @Transactional
    public void createLinkStatistics(Link link, Long lifeTimeInSeconds) {
        linkStatisticsRepository.save(
                LinkStatistics.builder()
                        .link(link)
                        .lifeTime(lifeTimeInSeconds)
                        .build()
        );
    }

//    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    public void addUsage(Link link) {
//        LinkStatistics linkStatistics = linkStatisticsRepository.findByLink(link)
//                .orElseThrow(() -> new LinkStatisticsNotExistsException(
//                        "link statistics for a link with id [%s] does not exists"
//                                .formatted(link.getId())));
//
//
//        linkStatistics.setCountOfUses(linkStatistics.getCountOfUses() + 1L);
//        linkStatisticsRepository.save(linkStatistics);
//
//    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean iskLifeTimeExpired(LinkStatistics linkStatistics) {
        Long lifeTime = linkStatistics.getLifeTime();
        if (linkStatistics
                .getDateOfCreation()
                .plus(lifeTime, ChronoUnit.SECONDS)
                .isBefore(LocalDateTime.now())) {
            linkRepository.delete(linkStatistics.getLink());
//            linkRepository.flush();
            return true;
        }
        return false;
    }
}
