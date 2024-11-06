package me.noteme.headhunting.domain.resume.controller;

import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.common.annotation.LoginUser;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import me.noteme.headhunting.common.response.SuccessResponse;
import me.noteme.headhunting.domain.resume.controller.request.ExperienceRequest;
import me.noteme.headhunting.domain.resume.dto.EnumTypeResponse;
import me.noteme.headhunting.domain.resume.dto.ExperienceResponse;
import me.noteme.headhunting.domain.resume.service.ExperienceService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/experience")
@RequiredArgsConstructor
public class ExperienceController {
    private final ExperienceService experienceService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse<Void> postExperience(
            @Validated @RequestBody ExperienceRequest request
    ) {
        experienceService.saveAllExperience(
                request.getExperiences()
        );

        return SuccessResponse.empty();
    }

    @GetMapping("")
    public SuccessResponse<ExperienceResponse> getExperiences(
            @RequestParam(name = "type", required = false) String type,
            @LoginUser Long memberId
    ) {
        return SuccessResponse.of(experienceService.getExperiences(memberId, type));
    }

    @DeleteMapping("/{experienceId}")
    public SuccessResponse<Void> deleteExperience(@PathVariable("experienceId") Long experienceId) {
        experienceService.deleteById(experienceId);

        return SuccessResponse.empty();
    }

    @GetMapping("/type")
    public SuccessResponse<List<EnumTypeResponse>> getExperienceTypes() {
        return SuccessResponse.of(experienceService.getExperienceTypes());
    }
}
