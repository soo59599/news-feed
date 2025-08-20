package org.nfactorial.newsfeed.domain.feed.service;

import java.util.List;
import java.util.stream.Collectors;

import org.nfactorial.newsfeed.common.code.ErrorCode;
import org.nfactorial.newsfeed.common.exception.BusinessException;
import org.nfactorial.newsfeed.domain.feed.dto.request.FeedPageRequest;
import org.nfactorial.newsfeed.domain.feed.dto.response.FeedAccountPostProjection;
import org.nfactorial.newsfeed.domain.feed.dto.response.FeedProfileInfoResponse;
import org.nfactorial.newsfeed.domain.feed.dto.response.FeedResponseProjection;
import org.nfactorial.newsfeed.domain.feed.dto.response.FeedResponse;
import org.nfactorial.newsfeed.domain.feed.dto.response.FeedSpecificAccountResponse;
import org.nfactorial.newsfeed.domain.feed.dto.response.FeedSpecificResponse;
import org.nfactorial.newsfeed.domain.feed.dto.response.PageResponseDto;
import org.nfactorial.newsfeed.domain.feed.repository.FeedRepository;
import org.nfactorial.newsfeed.domain.profile.entity.Profile;
import org.nfactorial.newsfeed.domain.profile.repository.ProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeedService {

	private final FeedRepository feedRepository;
	private final ProfileRepository profileRepository;

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

	//특정 사용자 피드 전체 조회
	public FeedSpecificAccountResponse findAccountPostAll(Long profileId,
		FeedPageRequest feedPageRequest) {

		Profile profile = profileRepository.findById(profileId)
			.orElseThrow(() -> new BusinessException(ErrorCode.PROFILE_NOT_FOUND));

		long offset = (feedPageRequest.getPageNumber() - 1) * feedPageRequest.getSize();
		long limit = feedPageRequest.getSize();

		List<FeedAccountPostProjection> accountPostAll = feedRepository.findAccountPostAll(offset, limit,
			profile.getId());

		Long feedCount = feedRepository.countAccountPostAll(profile.getId());

		FeedProfileInfoResponse feedProfileInfoResponse = FeedProfileInfoResponse.of(profile.getNickname(),
			profile.getFollowCount(), feedCount, profile.getMbti(),
			profile.getIntroduce());

		List<FeedSpecificResponse> currentPosts = accountPostAll.stream()
			.map(accountPost -> FeedSpecificResponse.of(
				accountPost.getNickname(), accountPost.getCreatedAt(), accountPost.getContent(),
				accountPost.getLikeCount(), accountPost.getCommentCount()
			)).collect(Collectors.toList());

		PageResponseDto<FeedSpecificResponse> postsPage = PageResponseDto.of(offset, limit,
			feedCount, currentPosts);

		return FeedSpecificAccountResponse.of(feedProfileInfoResponse, postsPage);
	}
}
