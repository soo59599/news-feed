package org.nfactorial.newsfeed.domain.interaction.service;

import org.nfactorial.newsfeed.common.code.ErrorCode;
import org.nfactorial.newsfeed.common.exception.BusinessException;
import org.nfactorial.newsfeed.domain.interaction.entity.Like;
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
	private final PostService postService;
	private final ProfileService profileService;

	@Transactional
	public void addLike(Long postId, Long profileId) {

		Profile currentProfile = profileService.getProfileById(profileId);
		Post currentPost = postService.getPostById(postId);

		if (likesRepository.existsByPostIdAndProfileId(postId, profileId)) {
			throw new BusinessException(ErrorCode.LIKE_ALREADY_EXISTS);
		}

		likesRepository.save(Like.of(currentPost, currentProfile));
		currentPost.incrementLikeCount();
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
