package me.noteme.headhunting.domain.resume.controller.request;

import jakarta.validation.Valid;
import lombok.Data;
import me.noteme.headhunting.domain.resume.entity.EducationalType;
import me.noteme.headhunting.domain.resume.entity.GradeType;

import java.time.LocalDate;
import java.util.List;

@Data
public class EducationalRequest {
    @Valid
    private List<EducationalForm> educationals;
}
