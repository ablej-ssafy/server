package me.noteme.headhunting.domain.resume.controller.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ResumeRequest {
    MultipartFile pdfFile;
}
