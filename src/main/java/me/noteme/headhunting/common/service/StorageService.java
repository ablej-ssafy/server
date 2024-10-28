package me.noteme.headhunting.common.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    void uploadFile(Long userId, String fileName, MultipartFile file);

    String getFileUrl(Long userId, String fileName);

}
