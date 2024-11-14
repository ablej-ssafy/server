package me.noteme.headhunting.common.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.noteme.headhunting.common.alarm.service.MattermostNotificationService;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import me.noteme.headhunting.domain.member.entity.Member;
import me.noteme.headhunting.domain.member.repository.MemberRepository;
import me.noteme.headhunting.domain.member.service.MemberService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduledService {
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    // 매일 오후 6시에 실행
    @Scheduled(cron = "0 0 18 * * ?", zone = "Asia/Seoul")
    public void getQuestion() {
        memberRepository.findAllByResumePdfAndMember()
                .forEach(member -> memberService.getQuestion(member.getId()));
        log.debug("사용자들을 위한 맞춤형 질문을 생성합니다.");
    }

    // 매일 오전 8시 30분에 실행
    @Scheduled(cron = "0 30 8 * * ?", zone = "Asia/Seoul")
    public void sendQuestion() {
        memberRepository.findAllByQuestionAndMember()
                .forEach(member -> memberService.sendQuestion(member.getId()));
        log.debug("사용자들을 위한 맞춤형 질문을 전송합니다.");
    }
}
