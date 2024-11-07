package me.noteme.headhunting.domain.resume.controller;

import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.common.annotation.LoginUser;
import me.noteme.headhunting.common.response.SuccessResponse;
import me.noteme.headhunting.domain.resume.controller.request.EducationalRequest;
import me.noteme.headhunting.domain.resume.dto.EducationalResponse;
import me.noteme.headhunting.domain.resume.dto.EnumTypeResponse;
import me.noteme.headhunting.domain.resume.service.EducationalService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/educational")
@RequiredArgsConstructor
public class EducationalController {
    private final EducationalService educationalService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse<Void> postEducational(
            @LoginUser Long memberId,
            @Validated @RequestBody EducationalRequest request
    ) {
        educationalService.saveAllEducationals(
                memberId,
                request.getEducationals()
        );

        return SuccessResponse.empty();
    }

    @GetMapping("")
    public SuccessResponse<EducationalResponse> getEducational(@LoginUser Long memberId) {
        return SuccessResponse.of(educationalService.getAllEducationals(memberId));
    }

    @GetMapping("/type")
    public SuccessResponse<List<EnumTypeResponse>> getEducationalType() {
        return SuccessResponse.of(educationalService.getEducationTypes());
    }

    @DeleteMapping("/{educationalId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEducational(@PathVariable("educationalId") Long educationalId) {
        educationalService.deleteById(educationalId);
    }
}
