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

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
//        yyyy-mm-dd HH24:MI:SS dsl
//        '2011-12-03T10:15:30' ldt
//        1970-01-01T18:06:17Z instant
//        yyyy-mm-dd : HH24-mi  BY_MINUTES

//        2024-03-28 15:16:13

//        'YYYY-MM-DD"T"HH24:MI:SS"Z"' гпт
        return switch (timeUnits) {

            case MINUTES -> clickLinkRepository.getStatByTimeUnitsOnDate(linkId,
                    StatTimeUnits.BY_MINUTES.getTimeUnits(), onDate.format(formatter));

            case HOURS -> clickLinkRepository.getStatByTimeUnitsOnDate(linkId,
                    StatTimeUnits.BY_HOURS.getTimeUnits(), onDate.format(formatter));

            case DAYS -> clickLinkRepository.getStatByDays(linkId);

        };
    }

}