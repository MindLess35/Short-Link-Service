package com.shortlink.webapp.domain.event;

import com.shortlink.webapp.domain.entity.link.LinkStatistics;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddLinkUsageEvent {
    private LinkStatistics linkStatistics;


}
