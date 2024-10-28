package me.noteme.headhunting.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;
import me.noteme.headhunting.common.entity.BaseEntity;

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
@ToString
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

    @Enumerated(EnumType.STRING)
    @Column(name = "provider_type", nullable = false)
    @Builder.Default
    private ProviderType providerType = ProviderType.LOCAL;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_type", nullable = false)
    @Builder.Default
    private RoleType roleType = RoleType.USER;

    @Column(name = "email_verified", nullable = false)
    @Builder.Default
    private boolean emailVerified = false;

    public void verify() {
        emailVerified = true;
    }
}
