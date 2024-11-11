package me.noteme.headhunting.common.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.noteme.headhunting.common.constant.PromptConst;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAiService {
    private final OpenAiChatModel openAiChatModel;

    public String autoResume(String userMessage) {
        Message ststemMessage = new SystemMessage(PromptConst.prompt);
        Message userMsg = new UserMessage(userMessage);

        Prompt prompt = new Prompt(List.of(ststemMessage, userMsg));
        return openAiChatModel.call(prompt)
                .getResults()
                .getFirst()
                .getOutput()
                .getContent();
    }
}
