package com.shortlink.webapp.controller;

import com.shortlink.webapp.dto.projection.ClickLinkStatProjection;
import com.shortlink.webapp.dto.projection.TopLinkSourceSitesProjection;
import com.shortlink.webapp.entity.enums.TimeUnits;
import com.shortlink.webapp.service.ClickLinkService;
import com.shortlink.webapp.service.LinkService;
import com.shortlink.webapp.service.LinkStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/links")
public class LinkStatisticsController {

    private final LinkStatisticsService linkStatisticsService;

    @GetMapping("/count-all-clicks")
    public ResponseEntity<Long> getCountAllClicks() {

        return ResponseEntity.ok(linkStatisticsService.getCountAllClicks());
    }

    @GetMapping("/top-sites")
    public ResponseEntity<List<TopLinkSourceSitesProjection>> getTopLinkSourceSites(
            @RequestParam(name = "top", defaultValue = "10") Short top) {

        return ResponseEntity.ok(linkStatisticsService.getTopLinkSourceSites(top));
    }


}
