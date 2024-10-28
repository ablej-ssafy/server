package me.noteme.headhunting.common.listener.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ConfirmEmailEvent {
    private String email;
    private String nickname;

    public static ConfirmEmailEvent of(String email, String nickname) {
        return new ConfirmEmailEvent(email, nickname);
    }
}
