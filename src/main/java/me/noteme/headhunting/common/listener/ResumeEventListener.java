package me.noteme.headhunting.common.listener;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.noteme.headhunting.common.listener.event.ResumeInitEvent;
import me.noteme.headhunting.domain.member.entity.Member;
import me.noteme.headhunting.domain.resume.entity.ResumeOrder;
import me.noteme.headhunting.domain.resume.entity.mongo.MongoResume;
import me.noteme.headhunting.domain.resume.entity.Resume;
import me.noteme.headhunting.domain.resume.repository.MongoResumeRepository;
import me.noteme.headhunting.domain.resume.repository.ResumeOrderRepository;
import me.noteme.headhunting.domain.resume.repository.ResumeRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResumeEventListener {
    private final EntityManager em;
    private final ResumeRepository resumeRepository;
    private final ResumeOrderRepository resumeOrderRepository;
    private final MongoResumeRepository mongoResumeRepository;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void initResumeRecord(ResumeInitEvent event) {
        Resume resume = Resume.builder()
                .member(em.getReference(Member.class, event.getId()))
                .build();

        Resume saveResume = resumeRepository.save(resume);
        resumeOrderRepository.save(
                ResumeOrder.builder()
                        .resume(saveResume)
                        .build()
        );
        mongoResumeRepository.save(
                MongoResume.builder()
                        .memberId(event.getId())
                        .build()
        );
    }
}
