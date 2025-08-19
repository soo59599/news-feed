package org.nfactorial.newsfeed.domain.auth.dto;

import lombok.Builder;

@Builder
public record SignUpResult(
	long accountId,
	String nickname,
	String email
) {
}
