package org.nfactorial.newsfeed.domain.profile.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    @Column(nullable = false, unique = true)
    private String nickname;

    private String mbti;

    private String introduce;

    private int followCount;

    private LocalDateTime deletedAt;

    public Profile(String nickname, String mbti, String introduce) {
        this.nickname = nickname;
        this.mbti = mbti;
        this.introduce = introduce;
    }

    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }
}
