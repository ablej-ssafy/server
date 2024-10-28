package me.noteme.headhunting.common.listener.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class FileUploadEvent {
    private Long userId;
    private String uuid;
    private MultipartFile file;
}
