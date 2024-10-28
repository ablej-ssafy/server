package me.noteme.headhunting.common.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class SuccessResponse<T> extends BaseResponse<T> {
    @JsonIgnore
    protected List<CustomError> errors;

    private final static SuccessResponse<Void> EMPTY = new SuccessResponse<>();

    private SuccessResponse(){
        super(true, 200, "success");
    }

    private SuccessResponse(T data){
        super(true, 200, "success");
        this.data = data;
    }

    public static SuccessResponse<Void> empty(){
        return EMPTY;
    }

    public static <T> SuccessResponse<T> of(T data){
        return new SuccessResponse<T>(data);
    }
}
