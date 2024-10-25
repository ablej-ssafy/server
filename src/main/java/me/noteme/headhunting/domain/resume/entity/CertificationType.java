package me.noteme.headhunting.domain.resume.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CertificationType {
    QUALIFICATION("자격증"),
    LANGUAGE("어학");

    private final String name;
}
