package me.noteme.headhunting.domain.member.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import me.noteme.headhunting.common.service.EmailService;
import me.noteme.headhunting.common.service.OpenAiService;
import me.noteme.headhunting.common.service.StorageService;
import me.noteme.headhunting.domain.member.dto.LoginMemberResponse;
import me.noteme.headhunting.domain.member.entity.Member;
import me.noteme.headhunting.domain.member.entity.Question;
import me.noteme.headhunting.domain.member.repository.MemberRepository;
import me.noteme.headhunting.domain.member.repository.QuestionRepository;
import me.noteme.headhunting.domain.member.repository.ScrapRepository;
import me.noteme.headhunting.domain.recruitment.dto.CategoryResponse;
import me.noteme.headhunting.domain.recruitment.dto.RecruitmentSummaryResponse;
import me.noteme.headhunting.domain.recruitment.entity.JobCategory;
import me.noteme.headhunting.domain.recruitment.repository.JobCategoryRepository;
import me.noteme.headhunting.domain.resume.entity.ResumePdf;
import me.noteme.headhunting.domain.resume.repository.ResumePdfRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final JobCategoryRepository jobCategoryRepository;
    private final ResumePdfRepository resumePdfRepository;
    private final MemberRepository memberRepository;
    private final ScrapRepository scrapRepository;
    private final OpenAiService openAiService;
    private final StorageService storageService;

    private final EmailService emailService;

    private final Gson gson;
    private final QuestionRepository questionRepository;

    public LoginMemberResponse info(long memberId) {
        Member member = getMember(memberId);

        if (!member.isEmailVerified()) {
            throw new CustomException(ErrorCode.ACCESS_DENIED, "이메일 인증을 완료해주세요");
        }

        return LoginMemberResponse.of(
                member.getId(),
                member.getNickname(),
                member.getUsername(),
                member.getCareer(),
                CategoryResponse.fromEntity(member.getJobCategory())
        );
    }

    public List<RecruitmentSummaryResponse> scrapList(Long memberId) {
        return scrapRepository.findAllByMemberId(memberId).stream()
                .map(RecruitmentSummaryResponse::fromEntity)
                .toList();
    }

    @Transactional
    public void updateJobCategory(Long memberId, Long jobCategoryId) {
        Member member = getMember(memberId);
        JobCategory jobCategory = jobCategoryRepository.findById(jobCategoryId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 직무입니다."));

        member.changeJobCategory(jobCategory);
    }

    private Member getMember(Long memberId) {
        return memberRepository.findFetchById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
    }

    @Transactional
    public void getQuestion(Long memberId) {
        List<ResumePdf> resumePdfs = resumePdfRepository.findAllByMemberId(memberId);
        if (resumePdfs.isEmpty()) {
            return;
        }

        ResumePdf resumePdf = resumePdfs.getFirst();
        String resumeText = storageService.getData(memberId + "/" + resumePdf.getKey());

        if(!StringUtils.hasText(resumeText)){
            return;
        }

        String questionJson = openAiService.question(resumeText);
        List<String> questionTexts = gson.fromJson(
                gson.fromJson(questionJson, JsonObject.class)
                .getAsJsonArray("contents"), List.class);

        Member member = getMember(memberId);
        List<Question> questions = questionRepository.findAllByMemberId(member);
        while (questions.size() < 7) {
            questions.add(Question.builder().member(member).build());
        }

        for (int i = 0; i < 7; i++) {
            questions.get(i).updateQuestion(questionTexts.get(i));
        }

        questionRepository.saveAll(questions);
    }

    public void sendQuestion(Long memberId) {
        Member member = getMember(memberId);

        List<Question> questions = questionRepository.findAllByMemberId(member);
        if(questions.isEmpty()){
            return;
        }

        int index = LocalDate.now().getDayOfYear() % 7;
        Question question = questions.get(index);
        emailService.sendQuestionEmail(
                member.getUsername(), member.getNickname(), question.getQuestion()
        );
    }
}
