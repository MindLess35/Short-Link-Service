package com.shortlink.webapp.controller;

import com.shortlink.webapp.dto.response.RedirectLinkDto;
import com.shortlink.webapp.service.LinkRedirectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class LinkRedirectController {
    private final LinkRedirectService linkRedirectService;

    @GetMapping("/{short-link-name}")
    public ResponseEntity<?> redirectByOriginalLink(@PathVariable("short-link-name") String shortLinkName) {
        RedirectLinkDto redirectLinkDto = linkRedirectService.getOriginalLink(shortLinkName);
        if (redirectLinkDto.isKeyExists())
            return ResponseEntity
                    .status(HttpStatus.TEMPORARY_REDIRECT)
                    .body(redirectLinkDto.getShortLink());

        return linkRedirectService.buildResponseEntity(redirectLinkDto.getOriginalLink());
    }

    @GetMapping("api/v1/links/check-key")
    public ResponseEntity<?> checkKey(@RequestParam String shortLinkName,
                                      @RequestParam String key) {
        RedirectLinkDto redirectLinkDto = linkRedirectService.checkLinkKey(shortLinkName, key);

        return linkRedirectService.buildResponseEntity(redirectLinkDto.getOriginalLink());
    }
}