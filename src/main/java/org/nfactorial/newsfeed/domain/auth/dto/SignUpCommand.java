package org.nfactorial.newsfeed.domain.auth.dto;

import lombok.Builder;

@Builder
public record SignUpCommand(
	String email,
	String password,
	String nickname,
	String introduce,
	String mbti
) {
	public static SignUpCommand of(SignUpRequest signUpRequest) {
		return SignUpCommand.builder()
			.email(signUpRequest.email())
			.password(signUpRequest.password())
			.nickname(signUpRequest.nickname())
			.introduce(signUpRequest.introduce())
			.mbti(signUpRequest.mbti())
			.build();
	}
}
