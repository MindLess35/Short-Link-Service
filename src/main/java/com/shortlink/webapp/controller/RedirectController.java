package com.shortlink.webapp.controller;

import com.shortlink.webapp.service.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class RedirectController {

    private final LinkService linkService;
//    @Order(1)
//    @GetMapping(value = "/swagger-ui/**") // Исключение для Swagger UI
//    public String swaggerUi() {
//        return "forward:/swagger-ui/index.html";
//    }

    @GetMapping("/{short-link-name}/{key}")
    public ResponseEntity<HttpStatus> redirectWithKey(@PathVariable("short-link-name") String shortLinkName,
                                                      @PathVariable("key") String key) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, linkService.getOriginalLinkByKey(shortLinkName, key));

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
//        .location(URI.create())
    }

    @GetMapping("/{short-link-name}")
    public ResponseEntity<HttpStatus> redirectByOriginalLink(@PathVariable("short-link-name") String shortLinkName) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, linkService.getOriginalLink(shortLinkName));

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

}