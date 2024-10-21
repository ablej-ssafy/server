package me.noteme.headhunting.common.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomError {
    private String field;
    private String code;
    private String message;
    private String objectName;

    public static CustomError of(ObjectError error){
        return new CustomError(
                (error instanceof FieldError) ? ((FieldError) error).getField() : null,
                error.getCode(),
                error.getDefaultMessage(),
                error.getObjectName()
        );
    }
}
