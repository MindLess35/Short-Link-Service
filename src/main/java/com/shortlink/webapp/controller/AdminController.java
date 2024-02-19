package com.shortlink.webapp.controller;

import com.shortlink.webapp.dto.request.UserCreateEditDto;
import com.shortlink.webapp.dto.response.AllUsersReadDto;
import com.shortlink.webapp.dto.response.LinkReadDto;
import com.shortlink.webapp.dto.response.UserReadDto;
import com.shortlink.webapp.service.LinkService;
import com.shortlink.webapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final UserService userService;
    private final LinkService linkService;

    @GetMapping("/users")
    public ResponseEntity<List<AllUsersReadDto>> getAllUsers() {
        return ResponseEntity.ok().body(userService.findAllUsers());
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserReadDto> getUser(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(userService.findUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserReadDto> updateUser(@PathVariable("id") Long id,
                                                  @RequestBody UserCreateEditDto userCreateEditDto) {
        return ResponseEntity.ok().body(userService.updateUser(id, userCreateEditDto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserReadDto> changeUser(@PathVariable("id") Long id,
                                                  @RequestBody Map<String, Object> fields) {
        return ResponseEntity.ok().body(userService.changeUser(id, fields));
    }

    @DeleteMapping("{id}")
    private ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();

    }


}
