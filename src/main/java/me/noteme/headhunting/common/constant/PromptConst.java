package me.noteme.headhunting.common.constant;

public interface PromptConst {
    String resumePrompt = """
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
                        ]
                    }
                    Instructions:
                    1. Ensure all JSON syntax is correct without any additional symbols or slashes.
                    2. In the aiExperiences section:
                       - title should contain the name of the project or experience (e.g., "딸깍", "친환경 커스터마이징 쇼핑몰").
                       - affiliation should contain the role or position of the individual (e.g., "백엔드", "프론트엔드", "프로젝트 리더", "개발자").
                    3. Use the "YYYY-MM-DD" format for all dates that correspond to LocalDate. If the date is missing or listed as "진행중", replace it with today's date with the "YYYY-MM-DD" format.
                    4. Omit optional fields like `referenceUrl` or `grade` if no values are available for them.
                    5. In the description field:
                       - Use proper punctuation, but ensure there is no trailing comma at the end of each sentence.
                       - Avoid adding unnecessary characters or symbols.
                       - Please write everything as it is without adding or summarizing anything new
                       - Please don't miss any content from the resume
                       - Description should contain writer's project or experience content (e.g., "역할", "기술스택", "개발 내용", "프로젝트 내용", "성과", "개발사항")
                       - If description has content's title, please write the content's title in the right placer
                    6. Please put the '\\n' in order to fit each context.
                    7. Please write the words in the right place so that you don't miss the content
            """;

    String questionPrompt = """
            You are now in the role of an interviewer, and you need to write 7 interview questions for this applicant based on the resume below.
            Each question should specifically ask about the applicant's main experience, skills, project performance, leadership and communication skills.
            Responses should be provided in JSON format only, and only JSON structures should be included without additional grammar such as ```.
            Examples are in the following format:
            {"contents": [ "Question 1", "Question 2", "Question 3", ... "Question 7"]}
            Make sure each question is provided as a separate string, and make sure that the question is concise and a key point.
            Please answer in the same language as your resume language.
            """;
}
