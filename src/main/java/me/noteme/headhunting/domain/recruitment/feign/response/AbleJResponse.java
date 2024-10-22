package me.noteme.headhunting.domain.recruitment.feign.response;

import lombok.Data;

@Data
public class AbleJResponse<T>{
    private boolean success;
    private T data;
}
