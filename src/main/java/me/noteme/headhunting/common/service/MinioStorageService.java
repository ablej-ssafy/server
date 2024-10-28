package me.noteme.headhunting.common.service;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Profile("dev")
@Component
@RequiredArgsConstructor
public class MinioStorageService implements StorageService {
    private final MinioClient minioClient;

    @Value("${minio.endpoint}")
    private String minioEndpoint;

    @Value("${minio.bucket.images}")
    private String imagesBucketName;

    @Value("${minio.bucket.resume}")
    private String resumeBucketName;

    @Override
    public void uploadFile(String fileName, MultipartFile file) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(Objects.requireNonNull(fileName).toLowerCase().endsWith(".pdf") ? resumeBucketName : imagesBucketName)
                            .object(fileName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
        } catch (IOException | MinioException | GeneralSecurityException e) {
            throw new CustomException(ErrorCode.FILE_IO_ERROR);
        }
    }

    @Override
    public String getFileUrl(String fileName) {
        try {
            if (Objects.requireNonNull(fileName).toLowerCase().endsWith(".pdf")) {
                return minioClient.getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs.builder()
                                .bucket(resumeBucketName)
                                .object(fileName)
                                .method(Method.GET)
                                .expiry(2, TimeUnit.HOURS)
                                .build()
                );
            } else {
                return String.format("%s/%s/%s", minioEndpoint, imagesBucketName, fileName);
            }
        }  catch (IOException | MinioException | GeneralSecurityException e) {
            throw new CustomException(ErrorCode.FILE_IO_ERROR);
        }
    }
}
