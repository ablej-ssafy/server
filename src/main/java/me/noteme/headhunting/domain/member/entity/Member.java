package me.noteme.headhunting.domain.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.noteme.headhunting.common.entity.BaseEntity;
import me.noteme.headhunting.domain.job.entity.InterestJob;
import me.noteme.headhunting.domain.resume.entity.Resume;

@Entity
@Table(
        name = "member",
        uniqueConstraints = {
                @UniqueConstraint(name = "username_unique", columnNames = "username")
        }
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String nickname;

    @Column(name = "profile_image")
    private String profileImage;

    private int career;

    @Column(name = "email_verified", nullable = false)
    @Builder.Default
    private boolean emailVerified = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_type", nullable = false)
    @Builder.Default
    private RoleType roleType = RoleType.USER;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider_type", nullable = false)
    @Builder.Default
    private ProviderType providerType = ProviderType.LOCAL;

    //////////////////////////////

    @OneToOne(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Resume resume;

    @OneToOne(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private InterestJob interestJob;

}
