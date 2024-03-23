package com.shortlink.webapp.controller;

import com.shortlink.webapp.service.UserImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/{id}/image")
public class UserImageController {

    private final UserImageService userImageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    , produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public ResponseEntity<Resource> uploadImage(@RequestPart MultipartFile image,
                                                @PathVariable("id") Long userId) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userImageService.uploadProfileImage(image, userId));
//        return new org.springframework.core.io.InputStreamResource(inputStream);  <-- service result
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteImage(@PathVariable("id") Long userId) {
        userImageService.deleteImage(userId);
        return ResponseEntity.noContent().build();
    }

}

