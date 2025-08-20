package org.nfactorial.newsfeed.domain.interaction.service;

import org.nfactorial.newsfeed.common.code.ErrorCode;
import org.nfactorial.newsfeed.common.exception.BusinessException;
import org.nfactorial.newsfeed.domain.interaction.entity.Follow;
import org.nfactorial.newsfeed.domain.interaction.repository.FollowRepository;
import org.nfactorial.newsfeed.domain.profile.entity.Profile;
import org.nfactorial.newsfeed.domain.profile.service.ProfileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FollowService {

	private final FollowRepository followRepository;
	private final ProfileService profileService;

	@Transactional
	public void followProfile(Long followerId, Long followingId) {

		if (followerId.equals(followingId)) {
			throw new BusinessException(ErrorCode.CANNOT_FOLLOW_SELF);
		}

		Profile follower = profileService.getProfileById(followerId);
		Profile following = profileService.getProfileById(followingId);

		if (followRepository.existsByFollowerAndFollowing(follower, following)) {
			throw new BusinessException(ErrorCode.FOLLOWING_ALREADY_EXISTS);
		}

		followRepository.save(Follow.of(follower, following));
		follower.incrementFollowCount();
	}

	@Transactional
	public void unFollowProfile(long followerId, Long followingId) {

		Profile follower = profileService.getProfileById(followerId);
		Profile following = profileService.getProfileById(followingId);

		Follow savedFollow = followRepository.findByFollowerAndFollowing(follower, following)
			.orElseThrow(() -> new BusinessException(ErrorCode.FOLLOWING_NOT_FOUND));

		followRepository.delete(savedFollow);
		follower.decrementFollowCount();
	}
}
