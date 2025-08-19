package org.nfactorial.newsfeed.domain.auth.dto;

import lombok.Builder;

@Builder
public record SignUpResponse(
	long accountId,
	String nickname,
	String email
) {
	public static SignUpResponse of(SignUpResult signUpResult) {
		return SignUpResponse.builder()
			.accountId(signUpResult.accountId())
			.nickname(signUpResult.nickname())
			.email(signUpResult.email())
			.build();
	}
}
