package me.noteme.headhunting.common.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    void uploadFile(Long memberId, String fileName, MultipartFile file);

    void uploadFile(Long memberId, String fileName, String data);

    String getFileUrl(Long memberId, String fileName);

    String getData(String path);

    void delete(String path);
}
