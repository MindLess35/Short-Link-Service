package com.shortlink.webapp.service;

import com.shortlink.webapp.dto.projection.ProfileImageWithUserIdProjection;
import com.shortlink.webapp.dto.response.ImageCachingDto;
import com.shortlink.webapp.exception.DeleteImageException;
import com.shortlink.webapp.exception.ImageUploadException;
import com.shortlink.webapp.exception.UserNotExistsException;
import com.shortlink.webapp.property.MinioProperty;
import com.shortlink.webapp.repository.UserRepository;
import io.minio.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserImageService {

    private final MinioProperty minioProperty;
    private final MinioClient minioClient;
    private final UserRepository userRepository;

    @Transactional
    @CachePut(value = "image", key = "#userId")
    public ImageCachingDto uploadProfileImage(MultipartFile image, Long userId) {
        if (image.isEmpty())
            throw new ImageUploadException("Image must exists");

        ProfileImageWithUserIdProjection projection = userRepository.findProfileImageWithUserIdById(userId)
                .orElseThrow(() -> new UserNotExistsException(
                        "User with id %s does not exists".formatted(userId)));

        if (projection.getProfileImage() != null)
            throw new ImageUploadException("User with id %s already have image".formatted(userId));

        String profileImage = UUID.randomUUID().toString() + userId;
        String contentType = getContentTypeOrElseJPEG(image);

        try (InputStream inputStream = image.getInputStream()) {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(minioProperty.getBucket())
                    .object(profileImage)
                    .stream(inputStream, image.getSize(), -1)
                    .contentType(contentType)
                    .build());

            userRepository.updateProfileImageById(profileImage, userId);

            try (InputStream secondInputStream = image.getInputStream()) {
                return new ImageCachingDto(secondInputStream.readAllBytes(), getContentTypeOrElseJPEG(image));
            }
        } catch (Exception e) {
            throw new ImageUploadException(e);
        }
    }

    @Transactional
    @CacheEvict(value = "image", key = "#userId")
    public void deleteImage(Long userId) {
        String profileImage = userRepository.findProfileImageById(userId)
                .orElseThrow(() -> new DeleteImageException(
                        "User with id %s does not have image or user doesn't exists".formatted(userId)));
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(minioProperty.getBucket())
                    .object(profileImage)
                    .build());
        } catch (Exception e) {
            throw new DeleteImageException("Delete image failed, reason: ", e);
        }
        userRepository.updateProfileImageToNullById(userId);
    }

    @Cacheable(value = "image", key = "#userId")
    public ImageCachingDto getProfileImage(Long userId) {
        ProfileImageWithUserIdProjection projection = userRepository.findProfileImageWithUserIdById(userId)
                .orElseThrow(() -> new UserNotExistsException(
                        "User with id %s does not exists".formatted(userId)));

        if (projection.getProfileImage() == null)
            throw new ImageUploadException(("User with id %s does not have" +
                                            " image").formatted(userId));

        try {
            GetObjectResponse minioResponse = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(minioProperty.getBucket())
                    .object(projection.getProfileImage())
                    .build());

            return new ImageCachingDto(minioResponse.readAllBytes(), getContentTypeOrElseJPEG(minioResponse));

        } catch (Exception e) {
            throw new RuntimeException(e);//todo create exception for get image method
        }

    }

    @CachePut(value = "image", key = "#userId")
    public ImageCachingDto updateProfileImage(MultipartFile image, Long userId) {
        if (image.isEmpty())
            throw new ImageUploadException("Image must exists");//todo create exception for update image method

        String profileImage = userRepository.findProfileImageById(userId)
                .orElseThrow(() -> new UserNotExistsException(
                        "User with id %s does not exists".formatted(userId)));

        String contentType = getContentTypeOrElseJPEG(image);

        try (InputStream inputStream = image.getInputStream()) {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(minioProperty.getBucket())
                    .object(profileImage)
                    .stream(inputStream, image.getSize(), -1)
                    .contentType(contentType)
                    .build());

            try (InputStream secondInputStream = image.getInputStream()) {
                return new ImageCachingDto(secondInputStream.readAllBytes(), getContentTypeOrElseJPEG(image));
            }
        } catch (Exception e) {
            throw new ImageUploadException(e);
        }
    }

    public String getContentTypeOrElseJPEG(MultipartFile image) {
        String contentType = image.getContentType();
        return getContentType(contentType);
    }

    public String getContentTypeOrElseJPEG(GetObjectResponse image) {
        String contentType = image.headers().get(HttpHeaders.CONTENT_TYPE);
        return getContentType(contentType);
    }

    private String getContentType(String contentType) {
        return contentType == null
                ? MediaType.IMAGE_JPEG_VALUE
                : contentType;
    }
}
