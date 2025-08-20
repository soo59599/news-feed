package org.nfactorial.newsfeed.domain.post.controller;

import org.nfactorial.newsfeed.common.code.SuccessCode;
import org.nfactorial.newsfeed.common.dto.GlobalApiResponse;
import org.nfactorial.newsfeed.common.security.AuthProfile;
import org.nfactorial.newsfeed.common.security.AuthProfileDto;
import org.nfactorial.newsfeed.domain.post.dto.request.PostCreateRequest;
import org.nfactorial.newsfeed.domain.post.dto.request.PostUpdateRequest;
import org.nfactorial.newsfeed.domain.post.dto.response.PostCreateResponse;
import org.nfactorial.newsfeed.domain.post.dto.response.PostGetOneResponse;
import org.nfactorial.newsfeed.domain.post.dto.response.PostUpdateResponse;
import org.nfactorial.newsfeed.domain.post.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

	private final PostService postService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public GlobalApiResponse<PostCreateResponse> createPost(@AuthProfile AuthProfileDto currentUserProfile,
		@Valid @RequestBody PostCreateRequest request) {

		PostCreateResponse response = postService.save(request, currentUserProfile);

		return GlobalApiResponse.of(SuccessCode.CREATED, response);
	}

	@PatchMapping("/{postId}")
	@ResponseStatus(HttpStatus.OK)
	public GlobalApiResponse<PostUpdateResponse> update(@PathVariable Long postId,
		@AuthProfile AuthProfileDto currentUserProfile, @Valid @RequestBody PostUpdateRequest request) {

		PostUpdateResponse response = postService.update(postId, request, currentUserProfile);

		return GlobalApiResponse.of(SuccessCode.OK, response);
	}

	@GetMapping("/{postId}")
	@ResponseStatus(HttpStatus.OK)
	public GlobalApiResponse<PostGetOneResponse> findById(@PathVariable Long postId) {

		PostGetOneResponse response = postService.findById(postId);

		return GlobalApiResponse.of(SuccessCode.OK, response);
	}

	@DeleteMapping("/{postId}")
	@ResponseStatus(HttpStatus.OK)
	public GlobalApiResponse<Void> deleteById(@PathVariable Long postId,
		@AuthProfile AuthProfileDto currentUserProfile) {

		postService.deleteById(postId, currentUserProfile);

		return GlobalApiResponse.of(SuccessCode.OK, null);
	}

}
