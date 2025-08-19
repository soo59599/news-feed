package org.nfactorial.newsfeed.domain.profile.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.nfactorial.newsfeed.domain.auth.entity.Account;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    @Column(nullable = false, unique = true)
    private String nickname;

    private String mbti;

    private String introduce;

    private int followCount;

    private LocalDateTime deletedAt;

    public Profile(Account account, String nickname, String mbti, String introduce) {
        this.account = account;
        this.nickname = nickname;
        this.mbti = mbti;
        this.introduce = introduce;
    }
}
