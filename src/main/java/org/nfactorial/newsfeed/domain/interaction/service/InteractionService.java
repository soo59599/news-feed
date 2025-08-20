package org.nfactorial.newsfeed.domain.interaction.service;

import org.nfactorial.newsfeed.common.code.ErrorCode;
import org.nfactorial.newsfeed.common.exception.BusinessException;
import org.nfactorial.newsfeed.domain.interaction.entity.Like;
import org.nfactorial.newsfeed.domain.interaction.mock.ITMockLike;
import org.nfactorial.newsfeed.domain.interaction.mock.ITMockPost;
import org.nfactorial.newsfeed.domain.interaction.mock.ITMockPostRepository;
import org.nfactorial.newsfeed.domain.interaction.mock.ITMockProfile;
import org.nfactorial.newsfeed.domain.interaction.mock.ITMockProfileRepository;
import org.nfactorial.newsfeed.domain.interaction.repository.LikesRepository;
import org.nfactorial.newsfeed.domain.post.entity.Post;
import org.nfactorial.newsfeed.domain.post.service.PostService;
import org.nfactorial.newsfeed.domain.profile.entity.Profile;
import org.nfactorial.newsfeed.domain.profile.service.ProfileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InteractionService {

	private final LikesRepository likesRepository;
	private final ITMockProfileRepository profileRepository;
	private final ITMockPostRepository postRepository;
	private final PostService postService;
	private final ProfileService profileService;

	@Transactional
	public void addLike(Long postId, Long profileId) {

		// 사용자 가져오기, 검증 위임 -> Service 계층의 메소드이므로 profileService의 메소드에서 검증 수행
		// TODO: annotation을 통한 profile 엔티티 반환 시 삭제 예정, 영속성 컨텍스트를 위해 임시 사용
		ITMockProfile profile = profileRepository.findById(profileId)
			.orElseThrow(() -> new IllegalArgumentException("profile 도메인 엔티티 not found 커스텀 예외가 구현되면 대체됩니다."));

		// Post 가져오기, 검증 위임 -> Service 계층의 메소드이므로 postService의 메소드에서 검증 수행
		// TODO: PostService의 단일 post 조회 메소드 완성시 변경 예정, 영속성 컨텍스트를 위해 임시 사용
		ITMockPost post = postRepository.findById(postId)
			.orElseThrow(() -> new IllegalArgumentException("post 도메인 엔티티 not found 커스텀 예외가 구현되면 대체됩니다."));

		// 중복 확인
		if (likesRepository.existsByPostIdAndProfileId(postId, profile.getId())) {
			throw new BusinessException(ErrorCode.LIKE_ALREADY_EXISTS);
		}

		likesRepository.save(ITMockLike.of(post, profile));
		post.incrementLikeCount();
	}

	@Transactional
	public void cancelLike(Long postId, Long profileId) {

		Profile savedProfile = profileService.getProfileById(profileId);
		Post savedPost = postService.getPostById(postId);

		Like savedLike = likesRepository.findByPostAndProfile(savedPost, savedProfile)
			.orElseThrow(() -> new BusinessException(ErrorCode.LIKE_NOT_FOUND));

		likesRepository.delete(savedLike);
		savedPost.decrementLikeCount();
	}

}
