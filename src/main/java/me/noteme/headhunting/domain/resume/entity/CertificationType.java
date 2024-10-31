package me.noteme.headhunting.domain.resume.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum CertificationType {
    QUALIFICATION("자격증"),
    LANGUAGE("어학");

    private final String name;

    public static CertificationType from(String value) {
        return Arrays.stream(CertificationType.values())
                .filter(type -> type.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "자격 타입이 틀렸습니다."));
    }
}