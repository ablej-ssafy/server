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
    private static final String promptText = """
                    Convert the following resume text into structured JSON in this format:
                    {
                        aiBasic: {
                            title: String,
                            name: String,
                            email: String,
                            birth: LocalDate,
                            phone: String,
                            job: String,
                            introduce: String,
                            portfolioUrl: String
                        },
                        aiEducationals: [
                            {
                                name: String,
                                major: String,
                                category: ASSOCIATE_DEGREE | BACHELOR | MASTER | DOCTOR,
                                grade: Double,
                                gradeType: FOUR_POINT_ZERO | FOUR_POINT_THREE | FOUR_POINT_FIVE,
                                description: String,
                                startAt: LocalDate,
                                endAt: LocalDate
                            }
                        ],
                        aiExperiences: [
                            {
                                experienceType: COMPANY | PROJECT | ACTIVITY,
                                title: String,
                                affiliation: String,
                                startAt: LocalDate,
                                endAt: LocalDate,
                                description: String,
                                referenceUrl: String
                            }
                        ],
                        aiCertifications: [
                            {
                                name: String,
                                organization: String,
                                credential: String,
                                acquisitionAt: LocalDate,
                                grade: String,
                                certificationType: QUALIFICATION | LANGUAGE
                            }
                        ],
                        aiReferenceUrls: [
                            {
                                url: String
                            }
                        ]
                    }
                    Ensure valid JSON format without extra characters like backticks or quotes. Here is the resume text:
            """;


    public String auto(String question) {
        Prompt prompt = new Prompt(promptText + "\n" + question);
        log.debug(prompt.getContents());

        ChatResponse call = openAiChatModel.call(prompt);
        List<Generation> results = call.getResults();
        Generation first = results.getFirst();

        return first.getOutput().getContent();
    }
}
