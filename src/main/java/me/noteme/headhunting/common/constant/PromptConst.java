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
                    Ensure valid JSON format without extra characters like backticks or quotes. Here is the resume text: %s
            """;
}
