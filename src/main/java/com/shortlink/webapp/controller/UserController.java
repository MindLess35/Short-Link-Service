package com.shortlink.webapp.controller;

import com.shortlink.webapp.dto.request.ChangePasswordDto;
import com.shortlink.webapp.dto.request.UserCreateEditDto;
import com.shortlink.webapp.dto.response.AllLinksReadDto;
import com.shortlink.webapp.dto.response.UserReadDto;
import com.shortlink.webapp.entity.User;
import com.shortlink.webapp.service.LinkService;
import com.shortlink.webapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final LinkService linkService;

//    @PreAuthorize("hasRole('ADMIN')")
//    @GetMapping
//    public ResponseEntity<List<AllUsersReadDto>> getAllUsers() {
//        return ResponseEntity.ok().body(userService.findAllUsers());
//    }

    @GetMapping("/{id}")
    public ResponseEntity<UserReadDto> getUser(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(userService.findUserById(id));
    }

    @GetMapping("/{id}/links")
    public ResponseEntity<Page<AllLinksReadDto>> getAllUsersLinks(
            @PathVariable("id") Long id,
            @PageableDefault(
                    sort = "dateOfCreation",
                    direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(name = "short_link", required = false) String shortLink,
            @RequestParam(name = "original_link", required = false) String originalLink,
            @RequestParam(name = "date_of_creation_before", required = false) LocalDateTime dateOfCreation,
            @RequestParam(name = "date_of_last_uses_before", required = false) LocalDateTime dateOfLastUses,
            @RequestParam(name = "count_of_uses_goe", required = false) Long countOfUses
    ) {


        return ResponseEntity.ok(linkService.getAllUsersLinksByPageableAndFilter(
                id,
                pageable,
                shortLink,
                originalLink,
                dateOfCreation,
                dateOfLastUses,
                countOfUses
        ));
    }

    @PutMapping("/{id}")
    @PreAuthorize("@securityExpression.isCorrectUserAccess(#id)")
    public ResponseEntity<UserReadDto> updateUser(@PathVariable("id") Long id,
                                                  @RequestBody UserCreateEditDto userCreateEditDto) {
        return ResponseEntity.ok().body(userService.updateUser(id, userCreateEditDto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserReadDto> changeUser(@PathVariable("id") Long id,
                                                  @RequestBody Map<String, Object> fields) {
        return ResponseEntity.ok().body(userService.changeUser(id, fields));
    }

    @PatchMapping("/change-password")
    public ResponseEntity<HttpStatus> changePassword(@RequestBody ChangePasswordDto changePasswordDto,
                                                     @AuthenticationPrincipal User user) {
        userService.changePassword(changePasswordDto, user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();

    }

    @DeleteMapping("/{id}/links")
    public ResponseEntity<HttpStatus> deleteAllUsersLinks(@PathVariable("id") Long id) {
        linkService.deleteAllUsersLinks(id);
        return ResponseEntity.noContent().build();

    }
}

