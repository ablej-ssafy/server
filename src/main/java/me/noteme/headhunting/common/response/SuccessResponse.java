package me.noteme.headhunting.common.response;

public class SuccessResponse<T> extends BaseResponse<T> {

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
