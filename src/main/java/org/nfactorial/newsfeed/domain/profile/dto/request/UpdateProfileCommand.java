package org.nfactorial.newsfeed.domain.profile.dto.request;

import lombok.Builder;

@Builder
public record UpdateProfileCommand(
	String nickname,
	String introduce,
	String mbti
) {
}
