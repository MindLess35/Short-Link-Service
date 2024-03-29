package com.shortlink.webapp.controller;

import com.shortlink.webapp.service.UserImageService;
import io.minio.GetObjectResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/{id}/image")
public class UserImageController {

    private final UserImageService userImageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    // todo HttpMediaTypeNotAcceptableException handler create for
    public ResponseEntity<byte[]> uploadImage(@RequestPart MultipartFile image,
                                              @PathVariable("id") Long userId) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.parseMediaType(userImageService.getContentTypeOrElseJPEG(image)))
                .body(userImageService.uploadProfileImage(image, userId));
    }

    @GetMapping(produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable("id") Long userId) {
        GetObjectResponse image = userImageService.getProfileImage(userId);

        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(userImageService.getContentTypeOrElseJPEG(image)))
                .body(userImageService.getContent(image));
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> updateImage(@RequestPart MultipartFile image,
                                              @PathVariable("id") Long userId) {

        return ResponseEntity
                .accepted()
                .contentType(MediaType.parseMediaType(userImageService.getContentTypeOrElseJPEG(image)))
                .body(userImageService.updateProfileImage(image, userId));
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteImage(@PathVariable("id") Long userId) {
        userImageService.deleteImage(userId);
        return ResponseEntity.noContent().build();
    }

}

