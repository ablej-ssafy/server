package me.noteme.headhunting.common.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.noteme.headhunting.common.listener.event.ResumeInitEvent;
import me.noteme.headhunting.domain.resume.service.ResumeService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResumeEventListener {
    private final ResumeService resumeService;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void initResumeRecord(ResumeInitEvent event) {
        resumeService.resumeInit(event.getId());
    }
}
