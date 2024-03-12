package com.shortlink.webapp.controller;

import com.shortlink.webapp.dto.response.AllUsersReadDto;
import com.shortlink.webapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<AllUsersReadDto>> getAllUsers(
            @PageableDefault(
                    sort = "dateOfCreation",
                    direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(name = "short_link", required = false) String shortLink,
            @RequestParam(name = "original_link", required = false) String originalLink,
            @RequestParam(name = "date_of_creation_before", required = false) LocalDateTime dateOfCreation,
            @RequestParam(name = "date_of_last_uses_before", required = false) LocalDateTime dateOfLastUses,
            @RequestParam(name = "count_of_uses_goe", required = false) Long countOfUses) {

        return ResponseEntity.ok(userService.findAllUsers());
    }

}
