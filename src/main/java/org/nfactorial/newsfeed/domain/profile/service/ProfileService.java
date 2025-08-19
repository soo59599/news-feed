package org.nfactorial.newsfeed.domain.profile.service;

import lombok.RequiredArgsConstructor;
import org.nfactorial.newsfeed.domain.auth.entity.Account;
import org.nfactorial.newsfeed.domain.profile.dto.request.CreateProfileCommand;
import org.nfactorial.newsfeed.domain.profile.entity.Profile;
import org.nfactorial.newsfeed.domain.profile.repository.ProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService implements ProfileServiceApi {

	private final ProfileRepository profileRepository;

	@Override
	public boolean isNicknameDuplicated(String nickname) {
		return profileRepository.existsByNickname(nickname);
	}

	@Override
	@Transactional
	public String createProfile(CreateProfileCommand createProfileCommand) {
		Account account = createProfileCommand.account();

		Profile newProfile = new Profile(
			account,
			createProfileCommand.nickname(),
			createProfileCommand.mbti(),
			createProfileCommand.introduce()
		);

		Profile savedProfile = profileRepository.save(newProfile);
		return savedProfile.getNickname();
	}
}