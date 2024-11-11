package me.noteme.headhunting.domain.resume.controller;

import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.common.annotation.LoginUser;
import me.noteme.headhunting.common.response.SuccessResponse;
import me.noteme.headhunting.domain.resume.controller.request.TechSkillRequest;
import me.noteme.headhunting.domain.resume.controller.request.TechStackRequest;
import me.noteme.headhunting.domain.resume.dto.TechResponse;
import me.noteme.headhunting.domain.resume.dto.TechSkillResponse;
import me.noteme.headhunting.domain.resume.service.TechService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tech")
@RequiredArgsConstructor
public class TechController {
    private final TechService techService;

    @PostMapping("/stack")
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse<Void> postTechStack(
            @LoginUser Long memberId,
            @Validated @RequestBody TechStackRequest request
    ) {
        techService.saveTechStack(
                memberId,
                request.getGithubUrl(),
                request.getNotionUrl(),
                request.getTechSkills(),
                request.getTechId()
        );

        return SuccessResponse.empty();
    }

    @PostMapping("/skill")
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse<Void> postTechSkill(@Validated @RequestBody TechSkillRequest request) {
        techService.saveTechSkill(
                request.getName(),
                request.getIconUrl()
        );

        return SuccessResponse.empty();
    }

    @GetMapping("/stack")
    public SuccessResponse<TechResponse> getTechStack(@LoginUser Long memberId) {
        return SuccessResponse.of(techService.getTechStack(memberId));
    }

    @GetMapping("/skill")
    public SuccessResponse<List<TechSkillResponse>> getAllTechSkills() {
        return SuccessResponse.of(techService.getAllTechSkills());
    }
}
