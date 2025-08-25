package org.nfactorial.newsfeed.domain.auth.dto;

import lombok.Builder;

@Builder
public record LoginCommand(
	String email,
	String password
) {
	public static LoginCommand of(LoginRequest loginRequest) {
		return LoginCommand.builder()
			.email(loginRequest.email())
			.password(loginRequest.password())
			.build();
	}
}
