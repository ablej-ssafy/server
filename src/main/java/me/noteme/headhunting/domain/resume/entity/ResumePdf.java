package me.noteme.headhunting.domain.resume.entity;

import jakarta.persistence.*;
import lombok.*;
import me.noteme.headhunting.common.entity.BaseEntity;
import me.noteme.headhunting.domain.member.entity.Member;

@Entity
@Table(name = "resume_pdf")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ResumePdf extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resume_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "file_name")
    private String fileName;

    private String key;

    public static ResumePdf of(Member member, String fileName, String key) {
        ResumePdf resumePdf = new ResumePdf();
        resumePdf.member = member;
        resumePdf.fileName = fileName;
        resumePdf.key = key;
        return resumePdf;
    }
}
