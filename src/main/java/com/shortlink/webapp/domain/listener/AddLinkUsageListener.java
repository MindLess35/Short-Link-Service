package com.shortlink.webapp.domain.listener;

import com.shortlink.webapp.domain.entity.link.ClickLink;
import com.shortlink.webapp.domain.entity.link.LinkStatistics;
import com.shortlink.webapp.domain.event.AddLinkUsageEvent;
import com.shortlink.webapp.repository.jpa.link.ClickLinkRepository;
import com.shortlink.webapp.repository.jpa.link.LinkStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class AddLinkUsageListener {
    private final LinkStatisticsRepository linkStatisticsRepository;
    private final ClickLinkRepository clickLinkRepository;

    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addLinkUsage(AddLinkUsageEvent event) {
        LinkStatistics linkStatistics = event.getLinkStatistics();
        linkStatistics.setCountOfUses(linkStatistics.getCountOfUses() + 1L);

        clickLinkRepository.save(ClickLink.builder()
                .link(linkStatistics.getLink())
                .usageTime(Instant.now())
                .build());
        linkStatisticsRepository.save(linkStatistics);
    }
}
