package org.nfactorial.newsfeed.domain.interaction.controller;

import org.nfactorial.newsfeed.common.code.SuccessCode;
import org.nfactorial.newsfeed.common.dto.GlobalApiResponse;
import org.nfactorial.newsfeed.common.security.AuthProfile;
import org.nfactorial.newsfeed.common.security.AuthProfileDto;
import org.nfactorial.newsfeed.domain.interaction.service.LikeService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class LikeController {

	private final LikeService likeService;

	@PostMapping("/api/v1/posts/{postId}/likes")
	public GlobalApiResponse<Void> addLike(
		@PathVariable Long postId,
		@AuthProfile AuthProfileDto currentProfile) {

		likeService.addLike(postId, currentProfile.profileId());
		return GlobalApiResponse.of(SuccessCode.CREATED, null);
	}

	@DeleteMapping("/api/v1/posts/{postId}/likes")
	public GlobalApiResponse<Void> cancelLike(
		@PathVariable Long postId,
		@AuthProfile AuthProfileDto currentProfile) {

		likeService.cancelLike(postId, currentProfile.profileId());
		return GlobalApiResponse.of(SuccessCode.OK, null);
	}
}
