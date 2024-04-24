package com.shortlink.webapp.controller.link;

import com.shortlink.webapp.dto.link.request.LinkCreateDto;
import com.shortlink.webapp.dto.link.request.LinkUpdateDto;
import com.shortlink.webapp.dto.link.request.AllLinksReadDto;
import com.shortlink.webapp.dto.link.response.LinkReadDto;
import com.shortlink.webapp.service.interfaces.link.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/links")
public class LinkController {

    private final LinkService linkService;

    @PostMapping
//    @PreAuthorize("permitAll")
    public ResponseEntity<LinkReadDto> createShortLink(@RequestBody @Validated LinkCreateDto linkCreateDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(linkService.createLink(linkCreateDto));
    }

    @GetMapping("/{id}")
//    @PreAuthorize("@securityExpression.isUserHasAccessToLink(#id)")
    public ResponseEntity<LinkReadDto> getLink(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(linkService.getLinkById(id));
    }

    @GetMapping
    public ResponseEntity<Page<AllLinksReadDto>> findAllLinksByPageableAndFilter(
            @PageableDefault(
                    sort = "dateOfCreation",
                    direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(name = "short_link", required = false) String shortLink,
            @RequestParam(name = "original_link", required = false) String originalLink,
            @RequestParam(name = "date_of_creation_before", required = false) Instant dateOfCreation,
            @RequestParam(name = "date_of_last_uses_before", required = false) Instant dateOfLastUses,
            @RequestParam(name = "count_of_uses_goe", required = false) Long countOfUses,
            @RequestParam(name = "user_exists", required = false) Boolean isUserExists,
            @RequestParam(name = "time_to_live_before", required = false) Instant timeToLive) {

        return ResponseEntity.ok(linkService.findAllLinksByPageableAndFilter(
                pageable,
                shortLink,
                originalLink,
                dateOfCreation,
                dateOfLastUses,
                countOfUses,
                isUserExists,
                timeToLive
        ));
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
                                                  @RequestBody LinkUpdateDto dto) {

        return ResponseEntity.ok(linkService.updateLink(id, dto));
    }


}
