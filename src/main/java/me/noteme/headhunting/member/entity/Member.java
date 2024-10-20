package me.noteme.headhunting.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
public class Member {
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
}
