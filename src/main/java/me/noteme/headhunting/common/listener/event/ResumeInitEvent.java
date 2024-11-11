package me.noteme.headhunting.common.listener.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class ResumeInitEvent {
    private Long id;
}
