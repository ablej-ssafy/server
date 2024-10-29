package me.noteme.headhunting.common.response;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public abstract class BaseResponse {
    private final boolean success;
    private final int code;
    private final String message;

    public BaseResponse(boolean isSuccess, int code, String message) {
        this.success = isSuccess;
        this.code = code;
        this.message = message;
    }
}
