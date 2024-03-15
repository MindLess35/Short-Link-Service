package com.shortlink.webapp.listener;

import com.shortlink.webapp.entity.ClickLink;
import com.shortlink.webapp.entity.Link;
import com.shortlink.webapp.entity.LinkStatistics;
import com.shortlink.webapp.event.AddLinkUsageEvent;
import com.shortlink.webapp.repository.ClickLinkRepository;
import com.shortlink.webapp.repository.LinkRepository;
import com.shortlink.webapp.repository.LinkStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AddLinkUsageListener {

    private final LinkStatisticsRepository linkStatisticsRepository;
    private final ClickLinkRepository clickLinkRepository;

    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addLinkUsage(AddLinkUsageEvent event) {

//        Page<Link> linksPage = linkRepository.findAllByOrderByDateOfCreationDesc(pageable);

        LinkStatistics linkStatistics = event.getLinkStatistics();
        linkStatistics.setCountOfUses(linkStatistics.getCountOfUses() + 1L);

        clickLinkRepository.save(ClickLink.builder()
                .link(linkStatistics.getLink())
                .usageTime(LocalDateTime.now())
                .build());
        linkStatisticsRepository.save(linkStatistics);
    }
}
