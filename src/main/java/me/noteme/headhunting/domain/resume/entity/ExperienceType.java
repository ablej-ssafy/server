package me.noteme.headhunting.domain.resume.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum ExperienceType {
    COMPANY("회사"),
    PROJECT("프로젝트"),
    ACTIVITY("대내외활동");

    private final String value;

    public static ExperienceType from(String value) {
        return Arrays.stream(ExperienceType.values())
                .filter(type -> type.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "경험 타입이 틀렸습니다."));
    }
}
