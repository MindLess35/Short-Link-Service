package com.shortlink.webapp.controller.user;

import com.shortlink.webapp.dto.user.response.ChangePasswordDto;
import com.shortlink.webapp.dto.user.response.UserUpdateDto;
import com.shortlink.webapp.dto.link.request.AllLinksReadDto;
import com.shortlink.webapp.dto.user.response.UserReadDto;
import com.shortlink.webapp.domain.entity.user.User;
import com.shortlink.webapp.service.interfaces.link.LinkService;
import com.shortlink.webapp.service.interfaces.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.history.Revision;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@Tag(name = "User Controller", description = "A controller for working with the user. The creation" +
                                             " of the user and the sing-in to the service is in the" +
                                             " Authentication Controller")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final LinkService linkService;

    @GetMapping("/{id}")
    @Operation(responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(name = "UserReadDto"))),
            @ApiResponse(responseCode = "404")
    }, summary = "Returns representation of user")
//    @PreAuthorize("@securityExpression.isCorrectUserAccess(#id)")
    public ResponseEntity<UserReadDto> getUser(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.findUserById(id));
    }

    @GetMapping("/{id}/links")
    @Operation(summary = "Returns list of user links representation with more information")
    public ResponseEntity<Page<AllLinksReadDto>> getAllUsersLinks(
            @PathVariable("id") Long id,
            @PageableDefault(
                    sort = "dateOfCreation",
                    direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(name = "short_link", required = false) String shortLink,
            @RequestParam(name = "original_link", required = false) String originalLink,
            @RequestParam(name = "date_of_creation_before", required = false) Instant dateOfCreation,
            @RequestParam(name = "date_of_last_uses_before", required = false) Instant dateOfLastUses,
            @RequestParam(name = "count_of_uses_goe", required = false) Long countOfUses,
            @RequestParam(name = "time_to_live_before", required = false) Instant timeToLive
    ) {

        return ResponseEntity.ok(linkService.getAllUsersLinksByPageableAndFilter(
                id,
                pageable,
                shortLink,
                originalLink,
                dateOfCreation,
                dateOfLastUses,
                countOfUses,
                timeToLive
        ));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Returns updated representation of user")
    public ResponseEntity<UserReadDto> updateUser(@PathVariable("id") Long id,
                                                  @RequestBody UserUpdateDto userUpdateDto) {
        return ResponseEntity.ok(userService.updateUser(id, userUpdateDto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserReadDto> changeUser(@PathVariable("id") Long id,
                                                  @RequestBody Map<String, Object> fields) {

        return ResponseEntity.accepted().body(userService.changeUser(id, fields));
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Returns updated representation of user")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/links")
    public ResponseEntity<HttpStatus> deleteAllUsersLinks(@PathVariable("id") Long id) {
        linkService.deleteAllUsersLinks(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{id}/history")
    public ResponseEntity<Page<Revision<Long, User>>> getUserAuditing(@PathVariable("id") Long id,
                                                                      @PageableDefault Pageable pageable) {

        return ResponseEntity.ok(userService.getUserAuditing(id, pageable));
    }

    @GetMapping("{id}/last-change")
    public ResponseEntity<Revision<Long, User>> getLastUserChange(@PathVariable("id") Long id) {

        return ResponseEntity.ok(userService.getLastUserChange(id));
    }

    @PatchMapping("{id}/change-password")
    public ResponseEntity<HttpStatus> changePassword(@PathVariable("id") Long id,
                                                     @RequestBody ChangePasswordDto changePasswordDto) {
        userService.changePassword(id, changePasswordDto);
        return ResponseEntity.accepted().build();
    }


}

