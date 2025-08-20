package org.nfactorial.newsfeed.domain.interaction.controller;

import java.util.List;

import org.nfactorial.newsfeed.common.code.SuccessCode;
import org.nfactorial.newsfeed.common.dto.GlobalApiResponse;
import org.nfactorial.newsfeed.common.security.AuthProfile;
import org.nfactorial.newsfeed.common.security.AuthProfileDto;
import org.nfactorial.newsfeed.domain.interaction.dto.response.FollowStatusResponse;
import org.nfactorial.newsfeed.domain.interaction.service.FollowService;
import org.nfactorial.newsfeed.domain.profile.dto.ProfileSummaryDto;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class FollowController {

	private final FollowService followService;

	@PostMapping("/api/v1/profiles/{followingId}/follows")
	public GlobalApiResponse<Void> followProfile(
		@PathVariable Long followingId,
		@AuthProfile AuthProfileDto currentProfile) {

		followService.followProfile(currentProfile.profileId(), followingId);
		return GlobalApiResponse.of(SuccessCode.CREATED, null);
	}

	@DeleteMapping("/api/v1/profiles/{followingId}/follows")
	public GlobalApiResponse<Void> unFollowProfile(
		@PathVariable Long followingId,
		@AuthProfile AuthProfileDto currentProfile) {

		followService.unFollowProfile(currentProfile.profileId(), followingId);
		return GlobalApiResponse.of(SuccessCode.OK, null);
	}

	@GetMapping("/api/v1/profiles/{followingId}/followed")
	public GlobalApiResponse<FollowStatusResponse> checkFollowStatus(
		@PathVariable Long followingId,
		@AuthProfile AuthProfileDto currentProfile
	) {
		
		FollowStatusResponse responseDto = followService.checkFollowStatus(currentProfile.profileId(), followingId);
		return GlobalApiResponse.of(SuccessCode.OK, responseDto);
	}

	@GetMapping("/api/v1/profiles/{followerId}/followings")
	public GlobalApiResponse<Slice<ProfileSummaryDto>> getFollowingProfiles(
		@PathVariable Long followerId) {

		List<ProfileSummaryDto> responseDto = followService.getFollowingProfiles(followerId);
		return GlobalApiResponse.of(SuccessCode.OK, responseDto);
	}

	@GetMapping("/api/v1/profiles/me/followings")
	public GlobalApiResponse<Slice<ProfileSummaryDto>> getMyFollowingProfiles(
		@AuthProfile AuthProfileDto currentProfile) {

		List<ProfileSummaryDto> responseDto = followService.getFollowingProfiles(currentProfile.profileId());
		return GlobalApiResponse.of(SuccessCode.OK, responseDto);
	}
}
