package org.nfactorial.newsfeed.domain.profile.service;

import org.nfactorial.newsfeed.domain.profile.dto.request.CreateProfileCommand;

public interface ProfileServiceApi {
	boolean isNicknameDuplicated(String nickname);

	long createProfile(CreateProfileCommand createProfileCommand);

	void deleteFromAccountId(long accountId);
}
