package org.nfactorial.newsfeed.domain.auth.entity;

import java.time.LocalDateTime;

import org.nfactorial.newsfeed.common.entity.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Account extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// 로그인용
	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	// 회원 탈퇴 확인용
	private LocalDateTime deletedAt;

	public static Account signUp(String email, String password) {
		return new Account(
			null,
			email,
			password,
			null
		);
	}
}
