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

@Profile("prod")
@Component
@RequiredArgsConstructor
public class GoogleStorageService implements StorageService {
    @Value("${spring.cloud.gcp.storage.credentials.location}")
    private String keyName;

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    @Override
    public void uploadFile(Long userId, String fileName, MultipartFile file) {
        if (file.isEmpty()) {
            return;
        }

        try (InputStream stream = ResourceUtils.getURL(keyName).openStream()) {
            String contentType = file.getContentType();

            Storage storage = StorageOptions.newBuilder()
                    .setCredentials(GoogleCredentials.fromStream(stream))
                    .build()
                    .getService();

            BlobInfo blob = BlobInfo.newBuilder(bucketName, path(userId, fileName))
                    .setContentType(contentType)
                    .build();

            storage.create(blob, file.getInputStream().readAllBytes());
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FAIL_UPLOAD);
        }
    }

    @Override
    public String getFileUrl(Long userId, String fileName) {
        return GoogleStorageConst.BASE_URL + bucketName + "/" + path(userId, fileName);
    }

    private String path(Long userId, String fileName) {
        return userId + "/" + fileName;
    }
}
