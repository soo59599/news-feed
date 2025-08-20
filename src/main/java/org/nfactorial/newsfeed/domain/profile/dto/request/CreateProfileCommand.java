package org.nfactorial.newsfeed.domain.profile.dto.request;

import lombok.Builder;

@Builder
public record CreateProfileCommand(
	String nickname,
	String introduce,
	String mbti
) {
}

