package me.noteme.headhunting.common.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    void uploadFile(String fileName, MultipartFile file);

    String getFileUrl(String fileName);

}
