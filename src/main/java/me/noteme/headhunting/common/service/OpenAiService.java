package me.noteme.headhunting.common.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.noteme.headhunting.common.constant.PromptConst;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAiService {
    private final OpenAiChatModel openAiChatModel;

    public String autoResume(String pdfText) {
        String promptInput = String.format(PromptConst.prompt, pdfText);
        Prompt prompt = new Prompt(promptInput);

        return openAiChatModel.call(prompt)
                .getResults()
                .getFirst()
                .getOutput()
                .getContent();
    }
}
