package org.nfactorial.newsfeed.domain.auth.mock;

import org.nfactorial.newsfeed.domain.auth.service.AuthProfileServiceApi;
import org.springframework.stereotype.Service;

// TODO: Profile Service 구현해주세요!!!
@Service
public class AuthMockProfileService implements AuthProfileServiceApi {
	@Override
	public boolean isNicknameDuplicated(String nickname) {
		return false;
	}

	@Override
	public String createProfile(CreateProfileCommand createProfileCommand) {
		return createProfileCommand.nickname();
	}

}
