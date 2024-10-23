package me.noteme.headhunting.common.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    boolean uploadFile(MultipartFile file);

    String getFileUrl(String fileName);

}
