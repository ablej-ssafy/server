package me.noteme.headhunting.common.listener.event;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileUploadEvent {
    private String uuid;
    private MultipartFile file;
}
