package me.noteme.headhunting.common.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.noteme.headhunting.common.listener.event.ConfirmEmailEvent;
import me.noteme.headhunting.common.listener.event.GithubAnalysisEvent;
import me.noteme.headhunting.common.service.EmailService;
import me.noteme.headhunting.common.utils.KeyUtils;
import me.noteme.headhunting.domain.member.repository.MemberCacheRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailEventListener {
    private final EmailService emailService;
    private final MemberCacheRepository memberCacheRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    public void sendEmail(ConfirmEmailEvent event) {
        String confirmKey = KeyUtils.generateKey();
        emailService.sendConfirmationEmail(event.getEmail(), event.getNickname(), confirmKey);

        memberCacheRepository.saveConfirmKey(confirmKey, event.getEmail());
    }

    @EventListener
    public void handleGithubRepositoryAnalysisCompleted(GithubAnalysisEvent event) {
        emailService.GithubRepositoryAnalysis(event.getEmail(), event.getNickname(), event.getRepositoryName(), event.getAnalysisSummary());
    }
}
