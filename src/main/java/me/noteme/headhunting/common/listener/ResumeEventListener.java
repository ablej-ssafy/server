package me.noteme.headhunting.common.listener;

import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.common.listener.event.ResumeInitEvent;
import me.noteme.headhunting.domain.member.repository.MemberRepository;
import me.noteme.headhunting.domain.resume.entity.Resume;
import me.noteme.headhunting.domain.resume.repository.ResumeRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ResumeEventListener {
    private final ResumeRepository resumeRepository;
    private final MemberRepository memberRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void initResumeRecord(ResumeInitEvent event) {
        resumeRepository.save(Resume.builder()
                .member(memberRepository.getReferenceById(event.getMemberId()))
                .build());
    }
}
