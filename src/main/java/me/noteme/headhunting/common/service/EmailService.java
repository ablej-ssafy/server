package me.noteme.headhunting.common.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final SpringTemplateEngine templateEngine;
    private final JavaMailSender mailSender;

    @Value("${app.client-base-url}")
    private String frontedUrl;

    private final String CONFIRM_TITLE = "[AHEY] 회원가입 이메일 인증 안내";
    private final String CONFIRM_VIEW_NAME = "confirm_member_account_mail";

    private final String QUESTION_TITLE = "[AHEY] 오늘의 면접 질문";
    private final String QUESTION_VIEW_NAME = "question_mail";

    private final String ANALYZE_TITLE = "[AHEY] 깃 프로젝트 분석 결과 안내";
    private final String ANALYZE_VIEW_NAME = "github_analysis_summation_mail";

    @Async("emailSendExecutor")
    public void sendConfirmationEmail(String to, String nickName, String key) {
        Context context = new Context();
        context.setVariable("nickname", nickName);
        context.setVariable("confirmURI", createURI("/verify", key));

        sendEmail(to, CONFIRM_TITLE, CONFIRM_VIEW_NAME, context);
    }

    public void sendQuestionEmail(String to, String nickName, String question) {
        Context context = new Context();
        context.setVariable("nickname", nickName);
        context.setVariable("question", question);
        context.setVariable("mainURI", frontedUrl);

        sendEmail(to, QUESTION_TITLE, QUESTION_VIEW_NAME, context);
    }

    @Async("emailSendExecutor")
    public void GithubRepositoryAnalysis(String to, String repositoryName, String analysisSummary) {
        Context context = new Context();
        context.setVariable("analysisSummary", analysisSummary);
        context.setVariable("subject", "[AHEY] " + repositoryName + " 분석 결과");

        sendEmail(to, ANALYZE_TITLE, ANALYZE_VIEW_NAME, context);
    }

    private void sendEmail(String to, String subject, String viewName, Context context) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setTo(to);
            message.setSubject(subject); // 제목 설정

            String index = templateEngine.process(viewName, context);
            message.setContent(index, "text/html;charset=utf-8");

            mailSender.send(message); // 메일 전송
        } catch (MessagingException e) {
            throw new CustomException(ErrorCode.SERVER_ERROR);
        }
    }

    private String createURI(String type, String key) {
        return UriComponentsBuilder.fromUriString(frontedUrl + type)
                .queryParam("key", key)
                .toUriString();
    }
}
