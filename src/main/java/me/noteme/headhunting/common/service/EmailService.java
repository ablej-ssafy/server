package me.noteme.headhunting.common.service;

import jakarta.mail.Address;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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

    public void sendConfirmationEmail(String to, String name, String key) {
        Context context = new Context();
        context.setVariable("nickname", name);
        String url = createURI("/confirm/email", key);
        context.setVariable("confirmURI", url);

        log.debug("url {}", url);
        sendEmail(to, "[AHEY] 회원가입 이메일 인증 안내", "index", context);
    }

    private String createURI(String type, String key) {
        return UriComponentsBuilder.fromUriString(frontedUrl + type + "/" + key).toUriString();
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
}
