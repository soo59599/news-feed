package org.nfactorial.newsfeed.domain.post.service;

import org.nfactorial.newsfeed.common.code.ErrorCode;
import org.nfactorial.newsfeed.common.exception.BusinessException;
import org.nfactorial.newsfeed.domain.post.dto.request.PostCreateRequest;
import org.nfactorial.newsfeed.domain.post.dto.request.PostUpdateRequest;
import org.nfactorial.newsfeed.domain.post.dto.response.PostCreateResponse;
import org.nfactorial.newsfeed.domain.post.dto.response.PostUpdateResponse;
import org.nfactorial.newsfeed.domain.post.entity.Post;
import org.nfactorial.newsfeed.domain.post.mock.MockAuthProfileDto;
import org.nfactorial.newsfeed.domain.post.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

	private final PostRepository postRepository;

	//TODO 프로필 받고 변경
	@Transactional
	public PostCreateResponse save(PostCreateRequest request, MockAuthProfileDto currentUserProfile) {

		Post savedPost = postRepository.save(Post.of(request, currentUserProfile));
		return PostCreateResponse.of(savedPost);
	}

	//TODO 프로필 받고 변경
	@Transactional
	public PostUpdateResponse update(Long postId, PostUpdateRequest request,
		MockAuthProfileDto currentUserProfile) {

		Post foundPost = postRepository.findById(postId)
			.orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

		// if (!ObjectUtils.nullSafeEquals(foundPost.getProfile().getId(), currentUserProfile.getId())) {
		// 	throw new BusinessException(ErrorCode.POST_ACCESS_DENIED);
		// }

		foundPost.updateContent(request);

		return PostUpdateResponse.of(foundPost);
	}
}
