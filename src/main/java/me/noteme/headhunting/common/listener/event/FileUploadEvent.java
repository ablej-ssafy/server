package me.noteme.headhunting.common.listener.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor(staticName = "of")
public class FileUploadEvent {
    private Long memberId;
    private MultipartFile file;
    private String fileText;
}
