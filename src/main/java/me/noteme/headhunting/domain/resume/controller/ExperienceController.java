package me.noteme.headhunting.domain.resume.controller;

import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.common.response.SuccessResponse;
import me.noteme.headhunting.domain.resume.controller.request.ExperienceForm;
import me.noteme.headhunting.domain.resume.controller.request.ExperienceRequest;
import me.noteme.headhunting.domain.resume.service.ExperienceService;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/experience")
@RequiredArgsConstructor
public class ExperienceController {
    private final ExperienceService experienceService;


    @PostMapping("/")
    public SuccessResponse<Void> postExperience(
            @Validated @RequestBody ExperienceRequest request,
            Errors errors) {
        experienceService.saveAllExperience(
                request.getExperiences()
        );

        return SuccessResponse.empty();
    }
}
