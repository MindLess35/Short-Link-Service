package com.shortlink.webapp.controller;

import com.shortlink.webapp.dto.user.response.AllUsersReadDto;
import com.shortlink.webapp.domain.enums.Role;
import com.shortlink.webapp.service.interfaces.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Admin Controller", description = "A controller for admin only, which allows" +
                                              " lock user and gets all users")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class AdminController {
    private final UserService userService;

    @GetMapping
    @Operation(summary = "Returns list of user representation with more information")
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
