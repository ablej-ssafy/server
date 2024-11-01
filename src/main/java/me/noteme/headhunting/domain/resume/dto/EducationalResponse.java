package me.noteme.headhunting.domain.resume.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.noteme.headhunting.domain.resume.controller.request.EducationalForm;

import java.util.List;

@Data
@AllArgsConstructor(staticName = "of")
public class EducationalResponse {
    private List<EducationalForm> educationals;
}
