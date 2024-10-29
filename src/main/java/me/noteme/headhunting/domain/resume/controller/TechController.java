package me.noteme.headhunting.domain.resume.controller;

import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import me.noteme.headhunting.common.response.SuccessResponse;
import me.noteme.headhunting.domain.resume.controller.request.TechSkillRequest;
import me.noteme.headhunting.domain.resume.controller.request.TechStackRequest;
import me.noteme.headhunting.domain.resume.service.TechService;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tech")
@RequiredArgsConstructor
public class TechController {
    private final TechService techService;

    @PostMapping("/stack")
    public SuccessResponse<Void> postTechStack(
            @Validated @RequestBody TechStackRequest request,
            Errors errors) {
        if (errors.hasErrors()) {
            throw new CustomException(ErrorCode.BAD_REQUEST, errors);
        }

        techService.saveTechStack(
                request.getResumeId(),
                request.getReferenceUrls(),
                request.getTechSkills(),
                request.getTechStackId()
        );

        return SuccessResponse.empty();
    }

    @PostMapping("/skill")
    public SuccessResponse<Void> postTechSkill(
            @Validated @RequestBody TechSkillRequest request,
            Errors errors
    ) {
        if (errors.hasErrors()) {
            throw new CustomException(ErrorCode.BAD_REQUEST, errors);
        }

        techService.saveTechSkill(
                request.getName(),
                request.getIconUrl()
        );

        return SuccessResponse.empty();
    }
}
