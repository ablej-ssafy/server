package me.noteme.headhunting.common.constant;

public interface PromptConst {
    String prompt = """
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
                    Instructions:
                    1. Ensure all JSON syntax is correct without any additional symbols or slashes.
                    2. In the aiExperiences section:
                       - title should contain the name of the project or experience (e.g., "딸깍", "친환경 커스터마이징 쇼핑몰").
                       - affiliation should contain the role or position of the individual (e.g., "Developer", "Project Leader").
                    3. Use the "YYY-MM-DD" format for all dates that correspond to LocalDate. If the date is missing or listed as "In Progress", replace it with today's date with the "YYY-MM-DD" format.
                    4. Omit optional fields like `referenceUrl` or `grade` if no values are available for them.
                    5. In the description field:
                       - Use proper punctuation, but ensure there is no trailing comma at the end of each sentence.
                       - Avoid adding unnecessary characters or symbols.
            """;
}
