package com.shortlink.webapp.controller.link;

import com.shortlink.webapp.dto.projection.link.TopLinkSourceSitesProjection;
import com.shortlink.webapp.service.interfaces.link.LinkStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
