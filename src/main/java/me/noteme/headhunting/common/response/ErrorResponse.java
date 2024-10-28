package me.noteme.headhunting.common.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import me.noteme.headhunting.common.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class ErrorResponse extends BaseResponse<Void> {
    @JsonIgnore
    protected Object data;

    public ErrorResponse(boolean isSuccess, int status, String message, Errors errors) {
        super(isSuccess, status, message);
        super.errors = parseErrors(errors);
    }

    private static ErrorResponse createErrorResponse(int status, String message, Errors errors) {
        return new ErrorResponse(false, status, message, errors);
    }

    private ErrorResponse(CustomException exception, String message) {
        this(false, exception.getErrorCode().getStatus().value(), message, exception.getErrors());
    }

    public static ErrorResponse of(CustomException exception) {
        return of(exception, exception.getMessage());
    }

    public static ErrorResponse of(CustomException exception, String message) {
        return createErrorResponse(exception.getErrorCode().getStatus().value(), message, exception.getErrors());
    }

    public static ErrorResponse of(Exception exception) {
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage(), null);
    }

    private List<CustomError> parseErrors(Errors errors) {
        return Optional.ofNullable(errors)
                .map(e -> Stream.concat(
                        e.getGlobalErrors().stream().map(CustomError::of),
                        e.getFieldErrors().stream().map(CustomError::of)
                ).toList())
                .orElseGet(List::of);
    }
}
