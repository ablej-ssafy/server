package me.noteme.headhunting.domain.resume.controller;

import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.common.annotation.LoginUser;
import me.noteme.headhunting.common.response.SuccessResponse;
import me.noteme.headhunting.domain.resume.controller.request.EducationForm;
import me.noteme.headhunting.domain.resume.controller.request.EducationRequest;
import me.noteme.headhunting.domain.resume.dto.EducationResponse;
import me.noteme.headhunting.domain.resume.dto.EnumTypeResponse;
import me.noteme.headhunting.domain.resume.service.EducationService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/education")
@RequiredArgsConstructor
public class EducationController {
    private final EducationService educationService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse<Void> postEducation(
            @LoginUser Long memberId,
            @Validated @RequestBody EducationRequest request
    ) {
        educationService.saveAllEducations(
                memberId,
                request.getEducations()
        );

        return SuccessResponse.empty();
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse<EducationForm> createEmptyEducation(
            @LoginUser Long memberId
    ) {
        return SuccessResponse.of(educationService.createEmptyEducation(memberId));
    }

    @GetMapping("")
    public SuccessResponse<EducationResponse> getEducation(@LoginUser Long memberId) {
        return SuccessResponse.of(educationService.getAllEducations(memberId));
    }

    @GetMapping("/type")
    public SuccessResponse<List<EnumTypeResponse>> getEducationType() {
        return SuccessResponse.of(educationService.getEducationTypes());
    }

    @DeleteMapping("/{educationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEducation(@PathVariable("educationId") Long educationId) {
        educationService.deleteById(educationId);
    }
}
