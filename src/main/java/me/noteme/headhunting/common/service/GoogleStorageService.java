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
    public void uploadFile(String fileName, MultipartFile file) {
        try (InputStream stream = ResourceUtils.getURL(keyName).openStream()) {

            if (file.isEmpty()) return;
            String contentType = file.getContentType();

            Storage storage = StorageOptions.newBuilder()
                    .setCredentials(GoogleCredentials.fromStream(stream))
                    .build()
                    .getService();

            BlobInfo blob = BlobInfo.newBuilder(bucketName, fileName)
                    .setContentType(contentType)
                    .build();

            storage.create(blob, file.getInputStream().readAllBytes());
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_IO_ERROR);
        }
    }

    @Override
    public String getFileUrl(String fileName) {
        return GoogleStorageConst.BASE_URL + bucketName + "/" + fileName;
    }
}
