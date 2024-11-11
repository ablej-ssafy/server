package me.noteme.headhunting.domain.resume.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.noteme.headhunting.domain.member.entity.Member;
import me.noteme.headhunting.domain.resume.entity.ResumeBasic;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class ResumeBasicResponse {
    private Long resumeBasicId;
    private String title;
    private String profile;
    private String name;
    private String email;
    private LocalDate birth;
    private String phone;
    private String job;
    private String introduce;
    private String portfolioUrl;

    public static ResumeBasicResponse fromEntity(ResumeBasic basic) {
        return ResumeBasicResponse.of(
                basic.getId(),
                basic.getTitle(),
                basic.getProfileImage(),
                basic.getName(),
                basic.getEmail(),
                basic.getBirth(),
                basic.getPhone(),
                basic.getJob(),
                basic.getIntroduce(),
                basic.getPortfolioUrl()
        );
    }

    public ResumeBasicResponse setInfoIfEmpty(Member member) {
        setFieldIfEmpty(this::getEmail, this::setEmail, member.getUsername());
        setFieldIfEmpty(this::getName, this::setName, member.getNickname());
        setFieldIfEmpty(this::getTitle, this::setTitle, member.getNickname() + "의 이력서");
        return this;
    }

    private void setFieldIfEmpty(Supplier<String> getter, Consumer<String> setter, String value){
        if(!StringUtils.hasText(getter.get())){
            setter.accept(value);
        }
    }
}
