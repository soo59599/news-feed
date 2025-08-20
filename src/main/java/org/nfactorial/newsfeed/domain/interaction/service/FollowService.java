package org.nfactorial.newsfeed.domain.interaction.service;

import java.util.List;

import org.nfactorial.newsfeed.common.code.ErrorCode;
import org.nfactorial.newsfeed.common.exception.BusinessException;
import org.nfactorial.newsfeed.domain.interaction.dto.response.FollowStatusResponse;
import org.nfactorial.newsfeed.domain.interaction.entity.Follow;
import org.nfactorial.newsfeed.domain.interaction.repository.FollowRepository;
import org.nfactorial.newsfeed.domain.profile.dto.ProfileSummaryDto;
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
	public void unFollowProfile(Long followerId, Long followingId) {

		Profile follower = profileService.getProfileById(followerId);
		Profile following = profileService.getProfileById(followingId);

		Follow savedFollow = followRepository.findByFollowerAndFollowing(follower, following)
			.orElseThrow(() -> new BusinessException(ErrorCode.FOLLOWING_NOT_FOUND));

		followRepository.delete(savedFollow);
		follower.decrementFollowCount();
	}

	@Transactional(readOnly = true)
	public FollowStatusResponse checkFollowStatus(Long followerId, Long followingId) {

		if (followerId.equals(followingId)) {
			throw new BusinessException(ErrorCode.CANNOT_FOLLOW_SELF);
		}

		// profileService의 메소드에 id 존재여부 검증 위임, 별도의 서비스 계층 exists 메소드 생성 x 목적
		Profile follower = profileService.getProfileById(followerId);
		Profile following = profileService.getProfileById(followingId);

		return FollowStatusResponse.of(followRepository.existsByFollowerAndFollowing(follower, following));
	}

	@Transactional(readOnly = true)
	public List<ProfileSummaryDto> getFollowingProfiles(Long followerId) {

		List<Long> followingIds = followRepository.findFollowingIdsByFollowerId(followerId);
		return profileService.findProfileSummariesByIds(followingIds);
	}
}
