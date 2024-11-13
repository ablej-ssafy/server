package me.noteme.headhunting.common.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.noteme.headhunting.common.constant.PromptConst;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAiService {
    private final OpenAiChatModel openAiChatModel;

    public String resume(String userMessage) {
        return generateResponse(PromptConst.resumePrompt, userMessage);
    }

    public String question(String systemMsg, String userMessage) {
        return generateResponse(systemMsg, userMessage);
    }

    private String generateResponse(String systemMsg, String userMessage) {
        Message systemMessage = new SystemMessage(systemMsg);
        Message userMsg = new UserMessage(userMessage);
        Prompt prompt = new Prompt(List.of(systemMessage, userMsg));

        return openAiChatModel.call(prompt)
                .getResults()
                .getFirst()
                .getOutput()
                .getContent();
    }
}
