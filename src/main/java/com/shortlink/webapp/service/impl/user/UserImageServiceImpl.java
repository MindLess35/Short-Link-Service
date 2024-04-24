package com.shortlink.webapp.service.impl.user;

import com.shortlink.webapp.dto.projection.user.ProfileImageWithUserIdProjection;
import com.shortlink.webapp.dto.user.ImageCachingDto;
import com.shortlink.webapp.domain.exception.base.ResourceNotFoundException;
import com.shortlink.webapp.domain.exception.user.ImageOperationException;
import com.shortlink.webapp.domain.property.MinioProperty;
import com.shortlink.webapp.repository.jpa.user.UserRepository;
import com.shortlink.webapp.service.interfaces.user.UserImageService;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
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
@CacheConfig(cacheResolver = "imageCacheResolver")
public class UserImageServiceImpl implements UserImageService {

    private final MinioProperty minioProperty;
    private final MinioClient minioClient;
    private final UserRepository userRepository;

    @Override
    @Transactional
    @CachePut(key = "#userId")
    public ImageCachingDto uploadProfileImage(MultipartFile image, Long userId) {
        if (image.isEmpty())
            throw new ImageOperationException("Image must exists");

        ProfileImageWithUserIdProjection projection = userRepository.findProfileImageWithUserIdById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with id %s does not exists".formatted(userId)));

        if (projection.getProfileImage() != null)
            throw new ImageOperationException("User with id %s already have image".formatted(userId));

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
            throw new ImageOperationException(e);
        }
    }
    @Override
    @Transactional
    @CacheEvict(key = "#userId")
    public void deleteImage(Long userId) {
        String profileImage = userRepository.findProfileImageById(userId)
                .orElseThrow(() -> new ImageOperationException(
                        "User with id %s does not have image or user doesn't exists".formatted(userId)));
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(minioProperty.getBucket())
                    .object(profileImage)
                    .build());
        } catch (Exception e) {
            throw new ImageOperationException("Delete image failed, reason: ", e);
        }
        userRepository.updateProfileImageToNullById(userId);
    }
    @Override
    @Cacheable(key = "#userId")
    public ImageCachingDto getProfileImage(Long userId) {
        ProfileImageWithUserIdProjection projection = userRepository.findProfileImageWithUserIdById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with id %s does not exists".formatted(userId)));

        if (projection.getProfileImage() == null)
            throw new ImageOperationException(("User with id %s does not have" +
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
    @Override
    @CachePut(key = "#userId")
    public ImageCachingDto updateProfileImage(MultipartFile image, Long userId) {
        if (image.isEmpty())
            throw new ImageOperationException("Image must exists");//todo create exception for update image method

        String profileImage = userRepository.findProfileImageById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
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
            throw new ImageOperationException(e);
        }
    }
    @Override
    public String getContentTypeOrElseJPEG(MultipartFile image) {
        String contentType = image.getContentType();
        return getContentType(contentType);
    }
    @Override
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
