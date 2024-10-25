package me.noteme.headhunting.common.listener;

import lombok.AllArgsConstructor;
import me.noteme.headhunting.common.listener.event.FileUploadEvent;
import me.noteme.headhunting.common.service.StorageService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class FileEventListener {
    private final StorageService storageService;

    @EventListener
    public void handleFileUploadEvent(FileUploadEvent event) {
        storageService.uploadFile(event.getFile());
    }
}
