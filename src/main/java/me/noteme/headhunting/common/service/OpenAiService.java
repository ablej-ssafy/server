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

    public String auto(String question) {
        Prompt prompt = new Prompt("You are provided with a raw resume text. Your task is to analyze and convert the information in the resume into a structured JSON format as follows:\n" +
                "\n" +
                "{\n" +
                "  \"data\": {\n" +
                "    \"basic\": {\n" +
                "      \"resumeId\": 1,\n" +
                "      \"resumeBasicId\": 10,\n" +
                "      \"title\": \"<Title>\",\n" +
                "      \"profile\": \"<profile_image_or_description>\",\n" +
                "      \"name\": \"<name>\",\n" +
                "      \"email\": \"<email>\",\n" +
                "      \"birth\": \"<date_of_birth_if_available>\",\n" +
                "      \"phone\": \"<phone>\",\n" +
                "      \"job\": \"<job_title>\",\n" +
                "      \"introduce\": \"<short_self_introduction>\",\n" +
                "      \"portfolioUrl\": \"<portfolio_url>\"\n" +
                "    },\n" +
                "    \"educationals\": [\n" +
                "      {\n" +
                "        \"resumeId\": 1,\n" +
                "        \"name\": \"<university_name>\",\n" +
                "        \"major\": \"<major>\",\n" +
                "        \"category\": \"<category>\",\n" +
                "        \"grade\": \"<grade>\",\n" +
                "        \"gradeType\": \"<grade_scale>\",\n" +
                "        \"description\": \"<brief_description_of_study>\",\n" +
                "        \"startAt\": \"<start_date>\",\n" +
                "        \"endAt\": \"<end_date>\",\n" +
                "        \"educationalId\": <unique_education_id>\n" +
                "      }\n" +
                "      // Repeat for other educational experiences\n" +
                "    ],\n" +
                "    \"projects\": [\n" +
                "      {\n" +
                "        \"resumeId\": 1,\n" +
                "        \"experienceType\": \"PROJECT\",\n" +
                "        \"title\": \"<project_title>\",\n" +
                "        \"affiliation\": \"<project_affiliation>\",\n" +
                "        \"startAt\": \"<project_start_date>\",\n" +
                "        \"endAt\": \"<project_end_date>\",\n" +
                "        \"description\": \"<project_description>\",\n" +
                "        \"referenceUrl\": \"<project_reference_url>\",\n" +
                "        \"experienceId\": <unique_project_id>\n" +
                "      }\n" +
                "      // Repeat for other projects\n" +
                "    ],\n" +
                "    \"activities\": [\n" +
                "      {\n" +
                "        \"resumeId\": 1,\n" +
                "        \"experienceType\": \"ACTIVITY\",\n" +
                "        \"title\": \"<activity_title>\",\n" +
                "        \"affiliation\": \"<activity_affiliation>\",\n" +
                "        \"startAt\": \"<activity_start_date>\",\n" +
                "        \"endAt\": \"<activity_end_date>\",\n" +
                "        \"description\": \"<activity_description>\",\n" +
                "        \"referenceUrl\": \"<activity_reference_url>\",\n" +
                "        \"experienceId\": <unique_activity_id>\n" +
                "      }\n" +
                "      // Repeat for other activities\n" +
                "    ],\n" +
                "    \"qualifications\": [\n" +
                "      {\n" +
                "        \"resumeId\": 1,\n" +
                "        \"name\": \"<qualification_name>\",\n" +
                "        \"organization\": \"<issuing_organization>\",\n" +
                "        \"credential\": \"<credential_info>\",\n" +
                "        \"acquisitionAt\": \"<acquisition_date>\",\n" +
                "        \"grade\": \"<grade>\",\n" +
                "        \"certificationType\": \"<type>\",\n" +
                "        \"certificationId\": <unique_certification_id>\n" +
                "      }\n" +
                "      // Repeat for other certifications\n" +
                "    ],\n" +
                "    \"languages\": [\n" +
                "      {\n" +
                "        \"resumeId\": 1,\n" +
                "        \"name\": \"<language>\",\n" +
                "        \"organization\": \"<organization_if_any>\",\n" +
                "        \"credential\": \"<credential_details>\",\n" +
                "        \"acquisitionAt\": \"<acquisition_date>\",\n" +
                "        \"grade\": \"<grade>\",\n" +
                "        \"certificationType\": \"<language_certification_type>\",\n" +
                "        \"certificationId\": <unique_language_certification_id>\n" +
                "      }\n" +
                "      // Repeat for other languages\n" +
                "    ],\n" +
                "    \"tech\": {\n" +
                "      \"resumeId\": 1,\n" +
                "      \"techId\": 1,\n" +
                "      \"techSkills\": [\n" +
                "        {\n" +
                "          \"skillId\": <skill_id>,\n" +
                "          \"skillName\": \"<skill_name>\",\n" +
                "          \"skillIcon\": \"<skill_icon_if_available>\"\n" +
                "        }\n" +
                "        // Repeat for other skills\n" +
                "      ],\n" +
                "      \"referenceUrls\": [\n" +
                "        {\n" +
                "          \"referenceUrlId\": <url_id>,\n" +
                "          \"url\": \"<url>\"\n" +
                "        }\n" +
                "        // Repeat for other URLs\n" +
                "      ]\n" +
                "    }\n" +
                "  }\n" +
                "}\n" +
                "\n" +
                "Transform the provided resume text into the JSON structure above, filling each field with the appropriate information extracted from the resume text.\n" +
                "Ensure that:\n" +
                "1. Dates are formatted as YYYY-MM-DD.\n" +
                "2. Text values are taken directly from the resume text, and for any missing information, leave the field as an empty string.\n" +
                "3. Assign unique numeric IDs (e.g., 1, 2, 3) for each educational, experience, project, and certification item under their respective ID fields.\n" +
                "4. Place unstructured resume sections (such as self-introductions, project descriptions, etc.) into the appropriate JSON fields.\n" +
                "\n" +
                "Given this format, here is the resume text:\n" +
                "---\n" +
                "\" 김용수\\r\\n akys159357@naver.com · 010-7185-1651 \\r\\n 백엔드 개발자 \\r\\n 대학 시절 3년동안...\"\n" +
                "---\n" +
                "Convert this into the specified JSON format.\n"
                + question);

        ChatResponse call = openAiChatModel.call(prompt);
        List<Generation> results = call.getResults();
        Generation first = results.getFirst();
        AssistantMessage output = first.getOutput();

        return output.getContent();
    }
}
