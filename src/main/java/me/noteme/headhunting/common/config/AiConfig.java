package me.noteme.headhunting.common.config;

import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {
    @Value("${spring.ai.openai.api-key}")
    private String key;

    @Bean
    public OpenAiChatModel getChatClient() {
        OpenAiApi openAiApi = new OpenAiApi(key);
        return new OpenAiChatModel(openAiApi);
    }
}
