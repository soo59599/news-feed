package org.nfactorial.newsfeed.domain.feed.service;

import java.util.List;
import java.util.stream.Collectors;

import org.nfactorial.newsfeed.domain.feed.dto.request.FeedPageRequest;
import org.nfactorial.newsfeed.domain.feed.dto.response.FeedResponseProjection;
import org.nfactorial.newsfeed.domain.feed.dto.response.FeedResponse;
import org.nfactorial.newsfeed.domain.feed.dto.response.PageResponseDto;
import org.nfactorial.newsfeed.domain.feed.repository.FeedRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeedService {

	private final FeedRepository feedRepository;

	//피드 전체 조회
	public PageResponseDto<FeedResponse> findAll(FeedPageRequest feedPageRequest) {
		long offset = (feedPageRequest.getPageNumber() - 1) * feedPageRequest.getSize();
		long limit = feedPageRequest.getSize();

		List<FeedResponseProjection> all = feedRepository.findPostWithNicknameAll(offset, limit);

		List<FeedResponse> feedResponseList = all.stream()
			.map(feed -> FeedResponse.of(
				feed.getNickname(), feed.getContent(),
				feed.getLikeCount(), feed.getCommentCount(),
				feed.getCreatedAt()
			)).collect(Collectors.toList());

		Long totalElements = feedRepository.countPostsAll();

		return PageResponseDto.of(offset, limit, totalElements, feedResponseList);
	}
}
