package org.nfactorial.newsfeed.domain.profile.dto.request;

import org.nfactorial.newsfeed.domain.auth.entity.Account;

import lombok.Builder;

@Builder
public record CreateProfileCommand(
	Account account,
	String nickname,
	String introduce,
	String mbti
) {
}

