package org.nfactorial.newsfeed.domain.feed.controller;

import org.nfactorial.newsfeed.common.code.SuccessCode;
import org.nfactorial.newsfeed.common.dto.GlobalApiResponse;
import org.nfactorial.newsfeed.common.security.AuthProfile;
import org.nfactorial.newsfeed.common.security.AuthProfileDto;
import org.nfactorial.newsfeed.domain.feed.dto.request.FeedPageRequest;
import org.nfactorial.newsfeed.domain.feed.dto.response.FeedFollowPostResponse;
import org.nfactorial.newsfeed.domain.feed.dto.response.FeedResponse;
import org.nfactorial.newsfeed.domain.feed.dto.response.FeedSpecificAccountResponse;
import org.nfactorial.newsfeed.domain.feed.dto.response.PageResponseDto;
import org.nfactorial.newsfeed.domain.feed.service.FeedService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FeedController {

	private final FeedService feedService;

	@GetMapping("/feeds")
	public GlobalApiResponse<PageResponseDto<FeedResponse>> findAll(@Valid FeedPageRequest feedPageRequest) {

		PageResponseDto<FeedResponse> feedAll = feedService.findAll(feedPageRequest);

		return GlobalApiResponse.of(SuccessCode.OK, feedAll);
	}

	@GetMapping("/profiles/{profileId}/feeds")
	public GlobalApiResponse<FeedSpecificAccountResponse> findAccountPostAll(
		@PathVariable("profileId") Long profileId,
		@Valid FeedPageRequest feedPageRequest) {

		FeedSpecificAccountResponse accountPostAll = feedService.findAccountPostAll(profileId, feedPageRequest);

		return GlobalApiResponse.of(SuccessCode.OK, accountPostAll);
	}

	@GetMapping("/feeds/following")
	public GlobalApiResponse<PageResponseDto<FeedFollowPostResponse>> findFollowPostAll(
		@AuthProfile AuthProfileDto authProfileDto,
		@Valid FeedPageRequest feedPageRequest) {

		PageResponseDto<FeedFollowPostResponse> followPostAll = feedService.findFollowPostAll(
			authProfileDto.profileId(), feedPageRequest);

		return GlobalApiResponse.of(SuccessCode.OK, followPostAll);
	}
}