package com.shortlink.webapp.controller;

import com.shortlink.webapp.dto.response.AllUsersReadDto;
import com.shortlink.webapp.entity.User;
import com.shortlink.webapp.entity.enums.Role;
import com.shortlink.webapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.history.Revision;
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
@RequestMapping("/api/v1/users")
public class AdminController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<AllUsersReadDto>> getAllUsersInPages(
            @PageableDefault(sort = "username") Pageable pageable,
            @RequestParam(name = "username", required = false) String username,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "role", required = false) Role role) {

        return ResponseEntity.ok(userService.findAllUsersByPageableAndFilter(
                pageable,
                username,
                email,
                role
        ));
    }


}
