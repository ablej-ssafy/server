package me.noteme.headhunting.common.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.noteme.headhunting.common.listener.event.FileUploadEvent;
import me.noteme.headhunting.common.service.StorageService;
import me.noteme.headhunting.common.utils.KeyUtils;
import me.noteme.headhunting.domain.resume.service.ResumeService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileEventListener {
    private final StorageService storageService;
    private final ResumeService resumeService;

    @EventListener
    public void handleFileUploadEvent(FileUploadEvent event) {
        String[] fileName = event.getFile().getOriginalFilename().split("\\.");
        String extension = fileName[fileName.length - 1];

        String key = KeyUtils.generateKey();
        storageService.uploadFile(event.getMemberId(), createFilename(key, extension), event.getFile());
        storageService.uploadFile(event.getMemberId(), key, event.getFileText());

        resumeService.savePdf(event.getMemberId(), fileName[0], key);
    }

    private String createFilename(String key, String extension) {
        return key + "." + extension;
    }
}
