package org.nfactorial.newsfeed.common.security.mock;

import java.time.LocalDateTime;

import org.nfactorial.newsfeed.domain.auth.entity.Account;

import lombok.Data;

@Data
public class SecurityMockProfile {
	private Long id;
	private Account account;
	private String nickname;
	private String mbti;
	private String introduce;
	private int followCount;
	private LocalDateTime deletedAt;
}
