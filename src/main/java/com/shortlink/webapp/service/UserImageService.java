package com.shortlink.webapp.service;

import com.shortlink.webapp.dto.projection.ProfileImageWithUserIdProjection;
import com.shortlink.webapp.exception.DeleteImageException;
import com.shortlink.webapp.exception.ImageUploadException;
import com.shortlink.webapp.exception.UserNotExistsException;
import com.shortlink.webapp.property.MinioProperty;
import com.shortlink.webapp.repository.UserRepository;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserImageService {

    private final MinioProperty minioProperty;
    private final MinioClient minioClient;
    private final UserRepository userRepository;

    @Transactional
    public Resource uploadProfileImage(MultipartFile image, Long userId) {
        if (image.isEmpty())
            throw new ImageUploadException("Image must exists");

        ProfileImageWithUserIdProjection projection = userRepository.findProfileImageWithUserIdById(userId)
                .orElseThrow(() -> new UserNotExistsException(
                        "User with id %s does not exists".formatted(userId)));

        String profileImage = UUID.randomUUID() + "-"
                              + image.getOriginalFilename();

        InputStream inputStream;
        try {
            inputStream = image.getInputStream();
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(minioProperty.getBucket())
                    .object(profileImage)
                    .stream(inputStream, image.getSize(), -1)
                    .build());
            inputStream = image.getInputStream();
        } catch (Exception e) {
            throw new ImageUploadException(e);
        }

        String previousProfileImage = projection.getProfileImage();
        if (previousProfileImage != null)
            deleteProfileImage(previousProfileImage);

        userRepository.updateProfileImageById(profileImage, userId);

        return new InputStreamResource(inputStream);
    }

    @Transactional
    public void deleteImage(Long userId) {
        String profileImage = userRepository.findProfileImageById(userId)
                .orElseThrow(() -> new DeleteImageException(
                        "User with id %s does not have image or user doesn't exists".formatted(userId)));

        deleteProfileImage(profileImage);
        userRepository.updateProfileImageToNullById(userId);
    }

    private void deleteProfileImage(String profileImage) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(minioProperty.getBucket())
                    .object(profileImage)
                    .build());
        } catch (Exception e) {
            throw new DeleteImageException("Delete image failed, reason: ", e);
        }
    }
}
