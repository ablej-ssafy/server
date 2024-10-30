package me.noteme.headhunting.common.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    void uploadFile(Long userId, String fileName, MultipartFile file);

    void uploadFile(Long userId, String fileName, String data);

    String getFileUrl(Long userId, String fileName);

    String getData(String path);
}
