package org.nfactorial.newsfeed.domain.post.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.nfactorial.newsfeed.common.code.ErrorCode;
import org.nfactorial.newsfeed.common.exception.BusinessException;
import org.nfactorial.newsfeed.common.security.AuthProfileDto;
import org.nfactorial.newsfeed.domain.comment.service.CommentServiceApi;
import org.nfactorial.newsfeed.domain.interaction.service.InteractionQueryServiceApi;
import org.nfactorial.newsfeed.domain.post.dto.PostCountDto;
import org.nfactorial.newsfeed.domain.post.dto.request.PostCreateRequest;
import org.nfactorial.newsfeed.domain.post.dto.request.PostUpdateRequest;
import org.nfactorial.newsfeed.domain.post.dto.response.PostCreateResponse;
import org.nfactorial.newsfeed.domain.post.dto.response.PostGetOneResponse;
import org.nfactorial.newsfeed.domain.post.dto.response.PostUpdateResponse;
import org.nfactorial.newsfeed.domain.post.entity.Post;
import org.nfactorial.newsfeed.domain.post.repository.PostRepository;
import org.nfactorial.newsfeed.domain.profile.entity.Profile;
import org.nfactorial.newsfeed.domain.profile.service.ProfileServiceApi;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService implements PostServiceApi {

	private final PostRepository postRepository;
	private final CommentServiceApi commentService;
	private final ProfileServiceApi profileService;
	private final InteractionQueryServiceApi interactionQueryService;

	@Transactional
	public PostCreateResponse save(PostCreateRequest request, AuthProfileDto currentUserProfile) {
		long profileId = currentUserProfile.profileId();
		Profile foundProfile = profileService.getProfileById(profileId);

		Post savedPost = postRepository.save(Post.of(request, foundProfile));

		return PostCreateResponse.of(savedPost);
	}

	@Transactional
	public PostUpdateResponse update(Long postId, PostUpdateRequest request,
		AuthProfileDto currentUserProfile) {

		Post foundPost = getPostById(postId);

		if (!ObjectUtils.nullSafeEquals(foundPost.getProfile().getId(), currentUserProfile.profileId())) {
			throw new BusinessException(ErrorCode.POST_ACCESS_DENIED);
		}

		foundPost.updateContent(request.content());

		return PostUpdateResponse.of(foundPost);
	}

	@Transactional
	public PostGetOneResponse viewPost(Long postId) {

		postRepository.incrementViewCount(postId);

		Post foundPost = getPostById(postId);

		boolean hasLikedPost = interactionQueryService.hasLikedPost(foundPost.getId(), foundPost.getProfile().getId());

		int commentCount = commentService.getCommentCount(foundPost);

		return PostGetOneResponse.of(foundPost, commentCount, hasLikedPost);
	}

	@Transactional
	public void deleteById(Long postId, AuthProfileDto currentUserProfile) {

		Post foundPost = getPostById(postId);

		if (!ObjectUtils.nullSafeEquals(foundPost.getProfile().getId(), currentUserProfile.profileId())) {
			throw new BusinessException(ErrorCode.POST_ACCESS_DENIED);
		}

		postRepository.delete(foundPost);
	}

	// 포스트 찾기
	@Override
	@Transactional(readOnly = true)
	public Post getPostById(long postId) {
		return postRepository.findById(postId)
			.orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
	}

	@Transactional(readOnly = true)
	public Map<Long, Long> countPostsByProfile(List<Profile> profiles) {
		if (profiles == null || profiles.isEmpty()) {
			return new HashMap<>();
		}

		// 1. DTO로 카운트 조회
		List<PostCountDto> postCounts = postRepository.countPostsByProfile(profiles);

		// 2. DTO를 Map으로 변환
		Map<Long, Long> countMap = postCounts.stream()
			.collect(Collectors.toMap(
				PostCountDto::profileId,
				PostCountDto::postCount
			));

		// 3. 포스트가 없는 프로필들도 0으로 포함
		return profiles.stream()
			.collect(Collectors.toMap(
				Profile::getId,
				profile -> countMap.getOrDefault(profile.getId(), 0L)
			));
	}

}
