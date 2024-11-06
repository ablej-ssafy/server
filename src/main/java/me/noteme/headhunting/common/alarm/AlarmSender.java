package me.noteme.headhunting.common.alarm;

import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.noteme.headhunting.common.alarm.dto.MatterMostMessageDto.Attachment;
import me.noteme.headhunting.common.alarm.dto.MatterMostMessageDto.Attachments;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlarmSender {
    @Value("${notification.mattermost.enabled}")
    private boolean mmEnabled;

    @Value("${notification.mattermost.webhook-url}")
    private String webhookUrl;

    private final RestTemplateBuilder restTemplateBuilder;
    private final MattermostProperties mmProperties;
    private final Gson gson;

    public void sendMessage(Exception exception, String uri, String params) {
        if (!mmEnabled){
            return;
        }

        try {
            mmProperties.updateFooter();
            Attachment attachment = Attachment.builder()
                    .channel(mmProperties.getChannel())
                    .authorIcon(mmProperties.getAuthorIcon())
                    .authorName(mmProperties.getAuthorName())
                    .color(mmProperties.getColor())
                    .pretext(mmProperties.getPretext())
                    .title(mmProperties.getTitle())
                    .text(mmProperties.getText())
                    .footer(mmProperties.getFooter())
                    .build();

            attachment.addExceptionInfo(exception, uri, params);
            Attachments attachments = new Attachments(attachment);
            attachments.addProps(exception);
            String payload = gson.toJson(attachments);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-type", MediaType.APPLICATION_JSON_VALUE);

            HttpEntity<String> entity = new HttpEntity<>(payload, headers);
            restTemplateBuilder.build().postForEntity(webhookUrl, entity, String.class);
        } catch (Exception e) {
            log.error("#### ERROR!! Notification Manager : {}", e.getMessage());
        }
    }
}