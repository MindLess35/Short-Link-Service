package com.shortlink.webapp.controller;

import com.shortlink.webapp.dto.request.FilterLink;
import com.shortlink.webapp.dto.request.LinkCreateEditDto;
import com.shortlink.webapp.dto.response.LinkReadDto;
import com.shortlink.webapp.entity.Link;
import com.shortlink.webapp.entity.LinkStatistics;
import com.shortlink.webapp.repository.LinkRepository;
import com.shortlink.webapp.repository.LinkStatisticsRepository;
import com.shortlink.webapp.service.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/links")
public class LinkController {

    private final LinkService linkService;
    private final LinkRepository linkRepository;
    private final LinkStatisticsRepository linkStatisticsRepository;

    @PostMapping
//    @PreAuthorize("permitAll")
    public ResponseEntity<LinkReadDto> createShortLink(@RequestBody @Validated LinkCreateEditDto linkCreateEditDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(linkService.createLink(linkCreateEditDto));
    }


//    @GetMapping
//    public ResponseEntity<List<LinkReadDto>> getAllLinks() {
//        return ResponseEntity.ok().body(linkService.getAllLinks());
//    }

    @GetMapping
    public ResponseEntity<Page<Link>> findAllByPageableAndFilter(@PageableDefault(
            page = 0,
            size = 10,
            sort = "shortLink",
            direction = Sort.Direction.ASC) Pageable pageable,
            @ModelAttribute FilterLink filterLink) {

        Page<Link> allByPageable = linkService.findAllByPageableAndFilter(pageable, filterLink);
        return ResponseEntity.ok().body(allByPageable);
    }

    @GetMapping("/without-user")
    public ResponseEntity<List<LinkReadDto>> getAllLinksWithoutUser() {
        return ResponseEntity.ok().body(linkService.getAllLinksWithoutUser());
    }

    @GetMapping("/with-user")
    public ResponseEntity<List<LinkReadDto>> getAllLinksWithUser() {
        return ResponseEntity.ok().body(linkService.getAllLinksWithUser());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> deleteLink(@PathVariable("id") Long id) {
        linkService.deleteLink(id);
        return ResponseEntity.noContent().build();

    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteAllUsersLinks(@PathVariable("id") Long userId) {
        linkService.deleteAllUsersLinks(userId);
        return ResponseEntity.noContent().build();

    }

    @PatchMapping("/{id}")
    public ResponseEntity<LinkReadDto> changeLink(@RequestParam("id") Long id,
                                                  @RequestBody Map<String, Object> fields
    ) {
        return ResponseEntity.ok().body(linkService.changeLink(id, fields));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LinkReadDto> updateLink(@RequestParam("id") Long id,
                                                  @RequestBody LinkCreateEditDto dto
    ) {
        return ResponseEntity.ok().body(linkService.updateLink(id, dto));
    }


}
