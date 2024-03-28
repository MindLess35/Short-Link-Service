package com.shortlink.webapp.controller;

import com.shortlink.webapp.service.UserImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.RedisTemplate;
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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> uploadImage(@RequestPart MultipartFile image,
                                                @PathVariable("id") Long userId) {

        String contentType = image.getContentType();
        if (contentType == null)
            contentType = MediaType.IMAGE_JPEG_VALUE;

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.parseMediaType(contentType))
                .body(userImageService.uploadProfileImage(image, userId));
    }

//    @GetMapping(produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
//    public ResponseEntity<Resource> getImage(@PathVariable("id") Long userId) {
//
//        Resource image = userImageService.uploadProfileImage(userId);
//
//        String contentType = image.getFilename();
//        if (contentType == null) {
//            contentType = MediaType.IMAGE_JPEG_VALUE;
//        }
//
//        return ResponseEntity
//                .ok()
//                .contentType(MediaType.parseMediaType(contentType))
//                .body(image);
//    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteImage(@PathVariable("id") Long userId) {
        userImageService.deleteImage(userId);
        return ResponseEntity.noContent().build();
    }

}

