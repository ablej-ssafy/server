package me.noteme.headhunting.common.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.common.constant.GoogleStorageConst;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Profile("prod")
@Component
@RequiredArgsConstructor
public class GoogleStorageService implements StorageService {
    @Value("${spring.cloud.gcp.storage.credentials.location}")
    private String keyName;

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    @Override
    public void uploadFile(Long memberId, String fileName, MultipartFile file) {
        if (file.isEmpty()) {
            return;
        }

        try (InputStream stream = ResourceUtils.getURL(keyName).openStream()) {
            String contentType = file.getContentType();

            Storage storage = StorageOptions.newBuilder()
                    .setCredentials(GoogleCredentials.fromStream(stream))
                    .build()
                    .getService();

            BlobInfo blob = BlobInfo.newBuilder(bucketName, path(memberId, fileName))
                    .setContentType(contentType)
                    .build();

            storage.create(blob, file.getInputStream().readAllBytes());
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_ERROR);
        }
    }

    @Override
    public void uploadFile(Long memberId, String fileName, String data) {
        try (InputStream stream = ResourceUtils.getURL(keyName).openStream()) {
            Storage storage = StorageOptions.newBuilder()
                    .setCredentials(GoogleCredentials.fromStream(stream))
                    .build()
                    .getService();

            BlobInfo blob = BlobInfo.newBuilder(bucketName, path(memberId, fileName))
                    .setContentType("text/plain")
                    .build();

            storage.create(blob, data.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_ERROR);
        }
    }

    @Override
    public String getFileUrl(Long memberId, String fileName) {
        return GoogleStorageConst.BASE_URL + bucketName + "/" + path(memberId, fileName);
    }

    @Override
    public String getData(String path) {
        try (InputStream stream = ResourceUtils.getURL(keyName).openStream()) {
            Storage storage = StorageOptions.newBuilder()
                    .setCredentials(GoogleCredentials.fromStream(stream))
                    .build()
                    .getService();

            return new String(
                    storage.readAllBytes(bucketName, path),
                    StandardCharsets.UTF_8
            );
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_ERROR);
        }
    }

    @Override
    public void delete(String path) {
        try (InputStream stream = ResourceUtils.getURL(keyName).openStream()) {
            Storage storage = StorageOptions.newBuilder()
                    .setCredentials(GoogleCredentials.fromStream(stream))
                    .build()
                    .getService();

            storage.delete(bucketName, path);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_ERROR);
        }
    }

    private String path(Long memberId, String fileName) {
        return memberId + "/" + fileName;
    }
}
