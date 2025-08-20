package org.nfactorial.newsfeed.domain.feed.controller;

import org.nfactorial.newsfeed.common.code.SuccessCode;
import org.nfactorial.newsfeed.common.dto.GlobalApiResponse;
import org.nfactorial.newsfeed.domain.feed.dto.request.FeedPageRequest;
import org.nfactorial.newsfeed.domain.feed.dto.response.FeedResponse;
import org.nfactorial.newsfeed.domain.feed.dto.response.PageResponseDto;
import org.nfactorial.newsfeed.domain.feed.service.FeedService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feeds")
public class FeedController {

	private final FeedService feedService;

	@GetMapping
	public GlobalApiResponse<PageResponseDto<FeedResponse>> findAll(@Valid FeedPageRequest feedPageRequest) {

		PageResponseDto<FeedResponse> feedAll = feedService.findAll(feedPageRequest);

		return GlobalApiResponse.of(SuccessCode.OK, feedAll);
	}
}