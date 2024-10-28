package me.noteme.headhunting.domain.resume.controller;

import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.common.response.SuccessResponse;
import me.noteme.headhunting.domain.resume.controller.request.TechSkillRequest;
import me.noteme.headhunting.domain.resume.controller.request.TechStackRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tech")
@RequiredArgsConstructor
public class TechController {

    @PostMapping("/stack")
    public SuccessResponse<Void> postTechStack(@RequestBody TechStackRequest request) {
        // TODO: TechStack 저장
        return SuccessResponse.empty();
    }

    @PostMapping("/skill")
    public SuccessResponse<Void> postTechSkill(@RequestBody TechSkillRequest request) {
        // TODO: 기술 아이콘 저장
        return SuccessResponse.empty();
    }
}
