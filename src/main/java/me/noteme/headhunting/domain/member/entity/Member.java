package me.noteme.headhunting.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;
import me.noteme.headhunting.common.entity.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "member",
        uniqueConstraints = {
                @UniqueConstraint(name = "username_unique", columnNames = "username")
        }
)
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Column(nullable = false)
    @Builder.Default
    private int career = 0;

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

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<InterestJob> interestJobs = new ArrayList<>();

    public void verify() {
        emailVerified = true;
    }

    public void addInterestJob(InterestJob interestJob) {
        interestJobs.add(interestJob);
    }
}
