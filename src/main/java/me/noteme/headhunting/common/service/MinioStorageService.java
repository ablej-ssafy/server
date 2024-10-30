package me.noteme.headhunting.common.service;

import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Profile("dev")
@Service
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
    public void uploadFile(Long userId, String fileName, MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            String contentType = file.getContentType();
            String bucketName = fileName.toLowerCase().endsWith(".pdf") ? resumeBucketName : imagesBucketName;

            upload(userId, path(userId, fileName), inputStream, contentType, bucketName, file.getSize());
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FAIL_UPLOAD);
        }
    }

    @Override
    public void uploadFile(Long userId, String fileName, String data) {
        byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
        try (InputStream inputStream = new ByteArrayInputStream(dataBytes)) {
            upload(userId, fileName, inputStream, "text/plain", resumeBucketName, dataBytes.length);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FAIL_UPLOAD);
        }
    }

    private void upload(Long userId, String fileName, InputStream inputStream, String contentType, String bucketName, long size) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(path(userId, fileName))
                            .stream(inputStream, size, -1)
                            .contentType(contentType)
                            .build()
            );
        } catch (MinioException | GeneralSecurityException | IOException e) {
            throw new CustomException(ErrorCode.FAIL_UPLOAD);
        }
    }

    @Override
    public String getFileUrl(Long userId, String fileName) {
        try {
            if (!fileName.toLowerCase().endsWith(".pdf")) {
                return String.format("%s/%s/%s", minioEndpoint, imagesBucketName, fileName);
            }

            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(resumeBucketName)
                            .object(path(userId,fileName))
                            .method(Method.GET)
                            .expiry(2, TimeUnit.HOURS)
                            .build()
            );
        } catch (IOException | MinioException | GeneralSecurityException e) {
            throw new CustomException(ErrorCode.FAIL_UPLOAD);
        }
    }

    @Override
    public String getData(String path) {
        try (GetObjectResponse response = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(resumeBucketName)
                        .object(path)
                        .build())) {
            return new String(response.readAllBytes(), StandardCharsets.UTF_8);
        } catch (MinioException | IOException | GeneralSecurityException e) {
            throw new CustomException(ErrorCode.FAIL_UPLOAD);
        }
    }

    private String path(Long userId, String fileName) {
        return String.format("%s/%s",userId, fileName);
    }
}
