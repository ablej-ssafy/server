package me.noteme.headhunting.common.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAiService {
    private final OpenAiChatModel openAiChatModel;

    public String test(String question){
        Prompt prompt = new Prompt("내가 지금부터 보내는 코드를 이런형식으로 보내줘 이건 예시야 \n" +
                "{\n" +
                "  \"basic\": {\n" +
                "    \"profile\": \"string\",\n" +
                "    \"title\": \"string\",\n" +
                "    \"name\": \"string\",\n" +
                "    \"email\": \"string\",\n" +
                "    \"birth\": \"date\",\n" +
                "    \"phone\": \"string\",\n" +
                "    \"introduce\": \"string\",\n" +
                "    \"portfolioUrl\": \"string\"\n" +
                "  },\n" +
                "  \"educationals\":[\n" +
                "    {\n" +
                "      \"name\": \"string\",\n" +
                "      \"major\": \"string\",\n" +
                "      \"category\": \"ASSOCIATE_DEGREE, BACHELOR, MASTER, DOCTOR\",\n" +
                "      \"grade\": \"string\",\n" +
                "      \"gradeType\": \"FOUR_POINT_ZERO, FOUR_POINT_THREE, FOUR_POINT_FIVE\",\n" +
                "      \"description\": \"string\",\n" +
                "      \"startAt\": \"date\",\n" +
                "      \"endAt\": \"date\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"experiences\":[\n" +
                "    {\n" +
                "      \"experienceType\": \"COMPANY, PROJECT, ACTIVITY\",\n" +
                "      \"title\": \"string\",\n" +
                "      \"affiliation\": \"string\",\n" +
                "      \"startAt\": \"date\",\n" +
                "      \"endAt\": \"date\",\n" +
                "      \"description\": \"string\",\n" +
                "      \"referenceUrl\": \"string\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"certifications\":[\n" +
                "    {\n" +
                "      \"name\": \"string\",\n" +
                "      \"organization\": \"string\",\n" +
                "      \"credential\": \"string\",\n" +
                "      \"acquisitionAt\": \"date\",\n" +
                "      \"grade\": \"string\",\n" +
                "      \"certificationType\": \"QUALIFICATION, LANGUAGE\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"tech\":{\n" +
                "    \"reference_url\": [\n" +
                "      \"string\",\n" +
                "      \"string\",\n" +
                "      \"string\"\n" +
                "    ],\n" +
                "    \"tech_skils\": [\n" +
                "      \"string\",\n" +
                "      \"string\",\n" +
                "      \"string\"\n" +
                "    ]\n" +
                "  }\n" +
                "}" +
                question);
        log.debug("토큰 내용 {}", prompt);

        ChatResponse call = openAiChatModel.call(prompt);
        List<Generation> results = call.getResults();
        Generation first = results.getFirst();
        AssistantMessage output = first.getOutput();

        return output.getContent();
    }
}
