package com.shortlink.webapp.event;

import com.shortlink.webapp.entity.LinkStatistics;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddLinkUsageEvent {
    private LinkStatistics linkStatistics;


}
