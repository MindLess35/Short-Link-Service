package com.shortlink.webapp.service;

import com.shortlink.webapp.dto.projection.ClickLinkStatProjection;
import com.shortlink.webapp.entity.enums.StatTimeUnits;
import com.shortlink.webapp.entity.enums.TimeUnits;
import com.shortlink.webapp.repository.ClickLinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClickLinkService {
    private final ClickLinkRepository clickLinkRepository;

    public List<ClickLinkStatProjection> getStatByTimeUnitsOnDate(Long linkId,
                                                                  LocalDate onDate,
                                                                  TimeUnits timeUnits) {
//"dd-MM-yyyy"
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        return switch (timeUnits) {

            case MINUTES -> clickLinkRepository.getStatByTimeUnitsOnDate(linkId,
                    StatTimeUnits.BY_MINUTES.getTimeUnits(), onDate.format(formatter));

            case HOURS -> clickLinkRepository.getStatByTimeUnitsOnDate(linkId,
                    StatTimeUnits.BY_HOURS.getTimeUnits(), onDate.format(formatter));

            case DAYS -> clickLinkRepository.getStatByDays(linkId);

        };
    }

}