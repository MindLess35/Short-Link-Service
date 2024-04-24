package com.shortlink.webapp.service.interfaces.link;

import com.shortlink.webapp.domain.enums.TimeUnits;
import com.shortlink.webapp.dto.projection.link.ClickLinkStatProjection;

import java.time.LocalDate;
import java.util.List;

public interface ClickLinkService {
    List<ClickLinkStatProjection> getStatByTimeUnitsOnDate(Long linkId,
                                                           LocalDate onDate,
                                                           TimeUnits timeUnits);
}
