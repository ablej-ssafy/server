package me.noteme.headhunting.domain.resume.controller;

import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.common.response.SuccessResponse;
import me.noteme.headhunting.domain.resume.controller.request.EducationalRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/educational")
@RequiredArgsConstructor
public class EducationalController {

    @PostMapping("/")
    public SuccessResponse<Void> postEducational(@RequestBody EducationalRequest request) {
        // TODO: 이력서 교육 저장
        return SuccessResponse.empty();
    }
}
