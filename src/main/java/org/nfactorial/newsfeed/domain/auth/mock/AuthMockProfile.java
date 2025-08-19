package org.nfactorial.newsfeed.domain.auth.mock;

import java.time.LocalDateTime;

import org.nfactorial.newsfeed.domain.auth.entity.Account;

public class AuthMockProfile {
	private Long id;
	private Account account;
	private String nickname;
	private String mbti;
	private String introduce;
	private int followCount;
	private LocalDateTime deletedAt;
}
