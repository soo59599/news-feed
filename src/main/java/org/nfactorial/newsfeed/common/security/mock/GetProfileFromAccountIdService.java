package org.nfactorial.newsfeed.common.security.mock;

import org.nfactorial.newsfeed.common.security.GetProfileFromAccountIdApi;
import org.springframework.stereotype.Service;

@Service
public class GetProfileFromAccountIdService implements GetProfileFromAccountIdApi {
	@Override
	public SecurityMockProfile execute(long accountId) {
		SecurityMockProfile profile = new SecurityMockProfile();
		profile.setId(1L);
		profile.setNickname("NICKNAME");
		return profile;
	}
}
