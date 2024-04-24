package com.shortlink.webapp.controller.link;

import com.shortlink.webapp.dto.projection.link.ClickLinkStatProjection;
import com.shortlink.webapp.domain.enums.TimeUnits;
import com.shortlink.webapp.service.interfaces.link.ClickLinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/links")
public class ClickLinkController {
    private final ClickLinkService clickLinkService;

    @GetMapping("/{id}/click-stat")
    public ResponseEntity<List<ClickLinkStatProjection>> getLinkStatByTimeUnitsOnDate(
            @PathVariable("id") Long id,
            @RequestParam(name = "on_date") LocalDate onDate,
            @RequestParam(name = "time_units") TimeUnits timeUnits) {

        return ResponseEntity.ok(clickLinkService.getStatByTimeUnitsOnDate(
                id,
                onDate,
                timeUnits)
        );
    }


}
