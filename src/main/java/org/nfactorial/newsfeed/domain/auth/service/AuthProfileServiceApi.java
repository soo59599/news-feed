package org.nfactorial.newsfeed.domain.auth.service;

import lombok.Builder;

public interface AuthProfileServiceApi {
	boolean isNicknameDuplicated(String nickname);

	String createProfile(CreateProfileCommand createProfileCommand);

	@Builder
	record CreateProfileCommand(
		long accountId,
		String nickname,
		String introduce,
		String mbti
	) {

	}
}
