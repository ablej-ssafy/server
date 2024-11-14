package me.noteme.headhunting.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import me.noteme.headhunting.common.listener.event.ConfirmEmailEvent;
import me.noteme.headhunting.common.listener.event.ResumeInitEvent;
import me.noteme.headhunting.domain.member.repository.MemberCacheRepository;
import me.noteme.headhunting.domain.member.repository.MemberRepository;
import me.noteme.headhunting.domain.member.security.JwtTokenProvider;
import me.noteme.headhunting.domain.member.dto.JwtToken;
import me.noteme.headhunting.domain.member.entity.Member;
import me.noteme.headhunting.domain.recruitment.entity.JobCategory;
import me.noteme.headhunting.domain.recruitment.repository.JobCategoryRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {
    private final MemberCacheRepository memberCacheRepository;
    private final MemberRepository memberRepository;
    private final JobCategoryRepository jobRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    private final ApplicationEventPublisher publisher;

    /**
     * 회원 가입 로직
     *
     * @param email    이메일
     * @param password 비밀번호
     * @param name     이름
     */
    // TODO: 사용자의 선호 직무는 단 1개입니다.
    @Transactional
    public void signUp(String email, String password, String name, int careerYear, long jobId) {
        if (memberRepository.findByUsername(email).isPresent()) {
            throw new CustomException(ErrorCode.BAD_REQUEST, "이미 존재하는 사용자입니다.");
        }

        JobCategory jobCategory = jobRepository.findById(jobId)
                .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST, "잘못된 직무 값입니다."));

        String encodedPassword = passwordEncoder.encode(password);

        Member member = Member.builder()
                .username(email)
                .password(encodedPassword)
                .nickname(name)
                .jobCategory(jobCategory)
                .career(careerYear)
                .build();

        memberRepository.saveAndFlush(member);

        // Resume 저장 로직
        publisher.publishEvent(ResumeInitEvent.of(member.getId()));

        // 이메일 전송 로직
        publisher.publishEvent(ConfirmEmailEvent.of(email, name));
    }

    /**
     * 이메일 인증
     *
     * @param key 이메일 인증 키
     */
    @Transactional
    public void verify(String key) {
        String email = memberCacheRepository.findEmailByConfirmKey(key);
        if (Objects.isNull(email)) {
            throw new CustomException(ErrorCode.EXPIRED_URL, "이미 만료된 링크입니다.");
        }

        if (memberRepository.findNicknameByUsername(email).isEmpty()) {
            throw new CustomException(ErrorCode.EXPIRED_URL, "이미 처리된 사용자입니다.");
        }
        memberRepository.verify(email);
    }

    public void resendEmail(long memberId) {
        Member member = memberRepository.findFetchById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "존재 하지 않는 사용자입니다."));
        if(member.isEmailVerified()){
            throw new CustomException(ErrorCode.BAD_REQUEST, "이미 처리된 사용자입니다.");
        }

        publisher.publishEvent(ConfirmEmailEvent.of(member.getUsername(), member.getNickname()));
    }

    public JwtToken signIn(String email, String password) {
        Member member = getMember(email);

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new CustomException(ErrorCode.BAD_REQUEST, "유효하지 않은 비밀번호 입니다.");
        }

        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_USER")
        );

        return tokenProvider.generate(member.getId(), authorities);
    }

    private Member getMember(String email) {
        return memberRepository.findByUsername(email)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 사용자입니다."));
    }

    public void signOut(Long memberId, String refreshToken) {
        validate(memberId, refreshToken);
        memberCacheRepository.saveBlackListKey(refreshToken);
    }

    public JwtToken refresh(Long memberId, String refreshToken) {
        validate(memberId, refreshToken);
        JwtToken jwtToken = tokenProvider.refreshToken(refreshToken);

        memberCacheRepository.saveBlackListKey(refreshToken);
        return jwtToken;
    }

    private void validate(Long memberId, String refreshToken) {
        Long tokenId = tokenProvider.parseMemberId(refreshToken);
        if(!Objects.equals(memberId, tokenId)){
            throw new CustomException(ErrorCode.AUTHENTICATION_FAILED, "유효하지 않는 토큰입니다.");
        }
        if (memberCacheRepository.findAuthenticationKey(refreshToken)) {
            throw new CustomException(ErrorCode.AUTHENTICATION_FAILED, "적절하지 않은 리프레시 토큰입니다.");
        }
    }
}
