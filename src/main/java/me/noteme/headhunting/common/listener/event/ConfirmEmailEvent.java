package me.noteme.headhunting.common.listener.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class ConfirmEmailEvent {
    private String email;
    private String nickname;
}
