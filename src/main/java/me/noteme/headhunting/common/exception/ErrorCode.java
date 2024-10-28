package me.noteme.headhunting.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Common
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러입니다. 관리자에게 문의해주세요."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "올바르지 않은 요청 입니다. 다시 한번 확인해주세요."),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다."),

    // Auth,
    EXPIRED_URL(HttpStatus.UNAUTHORIZED, "만료된 링크입니다."),

    // AI
    AI_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "AI 서버 에러입니다. 관리자이게 문의해주세요."),

    // FILE
    FAIL_UPLOAD(HttpStatus.INTERNAL_SERVER_ERROR, "PDF 업로드 중 오류가 발생했습니다."),

    ;

    private final HttpStatus status;
    private final String message;
}
