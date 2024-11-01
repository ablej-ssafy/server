package me.noteme.headhunting.domain.resume.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor(staticName = "of")
public class ResumePdfResponse {
    private Long id;
    private String fileName;
    private LocalDate createdAt;
}
