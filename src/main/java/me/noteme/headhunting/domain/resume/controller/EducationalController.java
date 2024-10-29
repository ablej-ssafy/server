package me.noteme.headhunting.domain.resume.controller;

import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import me.noteme.headhunting.common.response.SuccessResponse;
import me.noteme.headhunting.domain.resume.controller.request.EducationalRequest;
import me.noteme.headhunting.domain.resume.service.EducationalService;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/educational")
@RequiredArgsConstructor
public class EducationalController {

    private final EducationalService educationalService;

    @PostMapping("/")
    public SuccessResponse<Void> postEducational(
            @Validated @RequestBody EducationalRequest request,
            Errors errors) {
        if (errors.hasErrors()) {
            throw new CustomException(ErrorCode.BAD_REQUEST, errors);
        }

        educationalService.saveAllEducationals(
                request.getEducationals()
        );

        return SuccessResponse.empty();
    }
}
