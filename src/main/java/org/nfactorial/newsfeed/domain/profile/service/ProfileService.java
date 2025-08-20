package org.nfactorial.newsfeed.domain.profile.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.nfactorial.newsfeed.common.code.ErrorCode;
import org.nfactorial.newsfeed.common.exception.BusinessException;
import org.nfactorial.newsfeed.domain.post.dto.PostCountDto;
import org.nfactorial.newsfeed.domain.post.repository.PostRepository;
import org.nfactorial.newsfeed.domain.profile.dto.request.CreateProfileCommand;
import org.nfactorial.newsfeed.domain.profile.dto.request.UpdateProfileCommand;
import org.nfactorial.newsfeed.domain.profile.dto.ProfileSummaryDto;
import org.nfactorial.newsfeed.domain.profile.entity.Profile;
import org.nfactorial.newsfeed.domain.profile.repository.ProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService implements ProfileServiceApi {

	private final ProfileRepository profileRepository;
	private final PostRepository postRepository;

	@Override
	public boolean isNicknameDuplicated(String nickname) {
		return profileRepository.existsByNickname(nickname);
	}

	@Override
	@Transactional
	public long createProfile(CreateProfileCommand createProfileCommand) {

		Profile newProfile = new Profile(
			createProfileCommand.nickname(),
			createProfileCommand.mbti(),
			createProfileCommand.introduce()
		);

		Profile savedProfile = profileRepository.save(newProfile);
		return savedProfile.getId();
	}

	@Override
	@Transactional
	public void deleteFromAccountId(long accountId) {
		Profile profile = profileRepository.findById(accountId)
			.orElseThrow(() -> new BusinessException(ErrorCode.PROFILE_NOT_FOUND));

		profile.softDelete();
	}

	@Override
	@Transactional(readOnly = true)
	public Profile getProfileById(long profileId) {
		return profileRepository.findById(profileId)
			.orElseThrow(() -> new BusinessException(ErrorCode.PROFILE_NOT_FOUND));
	}

	@Override
	@Transactional
	public Profile updateProfile(long profileId, UpdateProfileCommand command) {
		Profile profile = profileRepository.findById(profileId)
			.orElseThrow(() -> new BusinessException(ErrorCode.PROFILE_NOT_FOUND));

		if (profileRepository.existsByNickname(command.nickname()) && !profile.getNickname().equals(command.nickname())) {
			throw new BusinessException(ErrorCode.NICKNAME_DUPLICATED);
		}

		profile.update(command.nickname(), command.mbti(), command.introduce());

		return profile;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProfileSummaryDto> findProfileSummariesByIds(List<Long> profileIds) {
		List<Profile> profiles = profileRepository.findAllById(profileIds);

		//모든 프로필의 게시물 수를 두 번의 쿼리(프로필용, 게시물용)
		Map<Long, Long> postCounts = postRepository.countPostsByProfile(profiles).stream()
			.collect(Collectors.toMap(
				PostCountDto::profileId,
				PostCountDto::postCount
			));

		return profiles.stream()
			.map(profile -> {
				long postCount = postCounts.getOrDefault(profile.getId(), 0L);
				return ProfileSummaryDto.of(profile, postCount);
			})
			.collect(Collectors.toList());
	}
}