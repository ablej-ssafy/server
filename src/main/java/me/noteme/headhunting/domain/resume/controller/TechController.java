package me.noteme.headhunting.domain.resume.controller;

import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.common.response.SuccessResponse;
import me.noteme.headhunting.domain.resume.controller.request.TechSkillRequest;
import me.noteme.headhunting.domain.resume.controller.request.TechStackRequest;
import me.noteme.headhunting.domain.resume.service.TechService;
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
    public SuccessResponse<Void> postTechStack(@RequestBody TechStackRequest request) {
        techService.saveTechStack(
                request.getResumeId(),
                request.getReferenceUrls(),
                request.getTechSkills(),
                request.getTechStackId()
        );

        return SuccessResponse.empty();
    }

    @PostMapping("/skill")
    public SuccessResponse<Void> postTechSkill(@RequestBody TechSkillRequest request) {
        techService.saveTechSkill(
                request.getName(),
                request.getIconUrl()
        );
        return SuccessResponse.empty();
    }
}
