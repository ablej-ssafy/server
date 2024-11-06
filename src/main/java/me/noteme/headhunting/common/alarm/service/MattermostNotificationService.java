package me.noteme.headhunting.common.alarm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.noteme.headhunting.common.alarm.AlarmSender;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MattermostNotificationService {
    private final AlarmSender matterMostSender;

    public void sendNotification(Exception e, String uri, String params) {
        matterMostSender.sendMessage(e, uri, params);
    }
}
