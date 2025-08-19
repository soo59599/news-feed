package org.nfactorial.newsfeed.domain.auth.service;

import org.springframework.stereotype.Service;

// TODO: Profile Service 구현해주세요!!!
@Service
public class ProfileService implements ProfileServiceApi {
	@Override
	public boolean isNicknameDuplicated(String nickname) {
		return false;
	}

	@Override
	public String createProfile(CreateProfileCommand createProfileCommand) {
		return createProfileCommand.nickname();
	}

}
