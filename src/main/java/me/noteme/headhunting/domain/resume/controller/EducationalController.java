package me.noteme.headhunting.domain.resume.controller;

import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.common.annotation.LoginUser;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import me.noteme.headhunting.common.response.SuccessResponse;
import me.noteme.headhunting.domain.resume.controller.request.EducationalRequest;
import me.noteme.headhunting.domain.resume.dto.EducationalResponse;
import me.noteme.headhunting.domain.resume.dto.EnumTypeResponse;
import me.noteme.headhunting.domain.resume.service.EducationalService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
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
            @Validated @RequestBody EducationalRequest request,
            Errors errors
    ) {
        if (errors.hasErrors()) {
            throw new CustomException(ErrorCode.BAD_REQUEST, errors);
        }

        educationalService.saveAllEducationals(
                request.getEducationals()
        );

        return SuccessResponse.empty();
    }

    @GetMapping("")
    public SuccessResponse<EducationalResponse> getEducational(
            @LoginUser Long memberId
    ) {
        EducationalResponse response = educationalService.getAllEducationals(memberId);

        return SuccessResponse.of(response);
    }

    @GetMapping("/type")
    public SuccessResponse<List<EnumTypeResponse>> getEducationalType() {
        List<EnumTypeResponse> response = educationalService.getEducationTypes();

        return SuccessResponse.of(response);
    }
}
