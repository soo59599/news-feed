package org.nfactorial.newsfeed.domain.profile.dto.response;

import lombok.Builder;

@Builder
public record ProfileResponse(
	String nickname,
	String mbti,
	String introduce,
	int followCount,
	int postCount
) {
}
