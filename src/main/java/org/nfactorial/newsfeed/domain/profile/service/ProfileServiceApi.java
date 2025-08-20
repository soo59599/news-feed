package org.nfactorial.newsfeed.domain.profile.service;

import org.nfactorial.newsfeed.domain.profile.dto.request.CreateProfileCommand;
import org.nfactorial.newsfeed.domain.profile.dto.request.UpdateProfileCommand;
import org.nfactorial.newsfeed.domain.profile.entity.Profile;

public interface ProfileServiceApi {
	boolean isNicknameDuplicated(String nickname);

	long createProfile(CreateProfileCommand createProfileCommand);

	void deleteFromAccountId(long accountId);

	Profile getProfileById(long profileId);

	Profile updateProfile(long profileId, UpdateProfileCommand command);
}
