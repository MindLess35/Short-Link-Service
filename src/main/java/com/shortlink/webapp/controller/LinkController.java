package com.shortlink.webapp.controller;

import com.shortlink.webapp.dto.request.LinkCreateEditDto;
import com.shortlink.webapp.dto.response.AllLinksReadDto;
import com.shortlink.webapp.dto.response.LinkReadDto;
import com.shortlink.webapp.dto.projection.TopLinkSourceSitesProjection;
import com.shortlink.webapp.service.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/links")
public class LinkController {

    private final LinkService linkService;

    @PostMapping
//    @PreAuthorize("permitAll")
    public ResponseEntity<LinkReadDto> createShortLink(@RequestBody LinkCreateEditDto linkCreateEditDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(linkService.createLink(linkCreateEditDto));
    }

    @GetMapping("/{id}")
    @PreAuthorize("@securityExpression.isUserHasAccessToLink(#id)")
    public ResponseEntity<LinkReadDto> getLink(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(linkService.getLinkById(id));
    }


//    @GetMapping
//    public ResponseEntity<List<LinkReadDto>> getAllLinks() {
//        return ResponseEntity.ok().body(linkService.getAllLinks());
//    }

    @GetMapping
    public ResponseEntity<Page<AllLinksReadDto>> findAllLinksByPageableAndFilter(
            @PageableDefault(
                    sort = "dateOfCreation",
                    direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(name = "short_link", required = false) String shortLink,
            @RequestParam(name = "original_link", required = false) String originalLink,
            @RequestParam(name = "date_of_creation_before", required = false) LocalDateTime dateOfCreation,
            @RequestParam(name = "date_of_last_uses_before", required = false) LocalDateTime dateOfLastUses,
            @RequestParam(name = "count_of_uses_goe", required = false) Long countOfUses,
            @RequestParam(name = "user_exists", required = false) Boolean isUserExists) {

        Page<AllLinksReadDto> allByPageable = linkService.findAllLinksByPageableAndFilter(
                pageable,
                shortLink,
                originalLink,
                dateOfCreation,
                dateOfLastUses,
                countOfUses,
                isUserExists
        );
        return ResponseEntity.ok(allByPageable);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteLink(@PathVariable("id") Long id) {
        linkService.deleteLink(id);
        return ResponseEntity.noContent().build();

    }

    @PatchMapping("/{id}")
    public ResponseEntity<LinkReadDto> changeLink(@PathVariable("id") Long id,
                                                  @RequestBody Map<String, Object> fields) {

        return ResponseEntity.ok(linkService.changeLink(id, fields));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LinkReadDto> updateLink(@PathVariable("id") Long id,
                                                  @RequestBody LinkCreateEditDto dto) {

        return ResponseEntity.ok(linkService.updateLink(id, dto));
    }




}
