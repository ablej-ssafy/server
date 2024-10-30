package me.noteme.headhunting.common.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.noteme.headhunting.common.listener.event.FileUploadEvent;
import me.noteme.headhunting.common.service.StorageService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileEventListener {
    private final StorageService storageService;

    @EventListener
    public void handleFileUploadEvent(FileUploadEvent event) {
        log.debug("업로드 진행 : {}",event.getFile().getOriginalFilename());
//        storageService.uploadFile(event.getUserId(), "test", event.getFile());
    }
}
