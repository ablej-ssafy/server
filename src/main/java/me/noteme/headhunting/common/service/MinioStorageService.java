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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
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
    public void uploadFile(Long memberId, String fileName, MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            String contentType = file.getContentType();
            String bucketName = fileName.toLowerCase().endsWith(".pdf") ? resumeBucketName : imagesBucketName;

            upload(memberId, fileName, inputStream, contentType, bucketName, file.getSize());
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_ERROR);
        }
    }

    @Override
    public void uploadFile(Long memberId, String fileName, String data) {
        byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
        try (InputStream inputStream = new ByteArrayInputStream(dataBytes)) {
            upload(memberId, fileName, inputStream, "text/plain", resumeBucketName, dataBytes.length);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_ERROR);
        }
    }

    private void upload(Long memberId, String fileName, InputStream inputStream, String contentType, String bucketName, long size) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(path(memberId, fileName))
                            .stream(inputStream, size, -1)
                            .contentType(contentType)
                            .build()
            );
        } catch (MinioException | GeneralSecurityException | IOException e) {
            throw new CustomException(ErrorCode.FILE_ERROR);
        }
    }

    @Override
    public String getFileUrl(Long memberId, String fileName) {
        try {
            if (!fileName.toLowerCase().endsWith(".pdf")) {
                return String.format("%s/%s/%s", minioEndpoint, imagesBucketName, fileName);
            }

            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(resumeBucketName)
                            .object(path(memberId, fileName))
                            .method(Method.GET)
                            .expiry(2, TimeUnit.HOURS)
                            .build()
            );
        } catch (IOException | MinioException | GeneralSecurityException e) {
            throw new CustomException(ErrorCode.FILE_ERROR);
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
            throw new CustomException(ErrorCode.FILE_ERROR);
        }
    }

    @Override
    public void delete(String path) {
        log.debug("삭제 {}", path);
        try {
            String bucketName = getBucketName(path);

            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(path)
                            .build()
            );
        } catch (MinioException | IOException | GeneralSecurityException e) {
            throw new CustomException(ErrorCode.FILE_ERROR, "파일 삭제 중 문제가 생겼습니다.");
        }
    }

    private String path(Long memberId, String fileName) {
        return String.format("%s/%s", memberId, fileName);
    }

    private String getBucketName(String path) {
        if (path.endsWith(".pdf") || !path.contains(".")) {
            return resumeBucketName;
        } else {
            return imagesBucketName;
        }
    }
}
