package org.nfactorial.newsfeed.domain.interaction.service;

import org.nfactorial.newsfeed.common.code.ErrorCode;
import org.nfactorial.newsfeed.common.exception.BusinessException;
import org.nfactorial.newsfeed.domain.interaction.entity.Like;
import org.nfactorial.newsfeed.domain.interaction.repository.LikeRepository;
import org.nfactorial.newsfeed.domain.post.entity.Post;
import org.nfactorial.newsfeed.domain.post.service.PostServiceApi;
import org.nfactorial.newsfeed.domain.profile.entity.Profile;
import org.nfactorial.newsfeed.domain.profile.service.ProfileServiceApi;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeService {

	private final LikeRepository likeRepository;
	private final PostServiceApi postService;
	private final ProfileServiceApi profileService;

	@Transactional
	public void addLike(Long postId, Long profileId) {

		Profile currentProfile = profileService.getProfileEntityById(profileId);
		Post currentPost = postService.getPostById(postId);

		if (likeRepository.existsByPostIdAndProfileId(postId, profileId)) {
			throw new BusinessException(ErrorCode.LIKE_ALREADY_EXISTS);
		}

		likeRepository.save(Like.of(currentPost, currentProfile));
		currentPost.incrementLikeCount();
	}

	@Transactional
	public void cancelLike(Long postId, Long profileId) {

		Profile savedProfile = profileService.getProfileEntityById(profileId);
		Post savedPost = postService.getPostById(postId);

		Like savedLike = likeRepository.findByPostAndProfile(savedPost, savedProfile)
			.orElseThrow(() -> new BusinessException(ErrorCode.LIKE_NOT_FOUND));

		likeRepository.delete(savedLike);
		savedPost.decrementLikeCount();
	}
}
