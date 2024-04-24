package com.shortlink.webapp.service.interfaces.user;

import com.shortlink.webapp.dto.user.ImageCachingDto;
import io.minio.GetObjectResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UserImageService {

    ImageCachingDto uploadProfileImage(MultipartFile image, Long userId);

    void deleteImage(Long userId);

    ImageCachingDto getProfileImage(Long userId);

    ImageCachingDto updateProfileImage(MultipartFile image, Long userId);

    String getContentTypeOrElseJPEG(MultipartFile image);

    String getContentTypeOrElseJPEG(GetObjectResponse image);

}
