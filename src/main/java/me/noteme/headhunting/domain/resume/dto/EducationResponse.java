package me.noteme.headhunting.domain.resume.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.noteme.headhunting.domain.resume.controller.request.EducationForm;

import java.util.List;

@Data
@AllArgsConstructor(staticName = "of")
public class EducationResponse {
    private List<EducationForm> educations;
}
