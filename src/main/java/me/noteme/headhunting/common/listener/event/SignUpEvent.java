package me.noteme.headhunting.common.listener.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SignUpEvent {
    private String email;
    private String nickname;

    public static SignUpEvent of(String email, String nickname) {
        return new SignUpEvent(email, nickname);
    }
}
