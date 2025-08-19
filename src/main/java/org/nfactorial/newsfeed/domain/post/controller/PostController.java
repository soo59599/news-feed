package org.nfactorial.newsfeed.domain.post.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.nfactorial.newsfeed.common.code.SuccessCode;
import org.nfactorial.newsfeed.common.dto.GlobalApiResponse;
import org.nfactorial.newsfeed.domain.post.dto.request.PostCreateRequest;
import org.nfactorial.newsfeed.domain.post.dto.response.PostCreateResponse;
import org.nfactorial.newsfeed.domain.post.mock.MockAuthProfileDto;
import org.nfactorial.newsfeed.domain.post.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

	private final PostService postService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	// public GlobalApiResponse<PostCreateResponse> createPost(@AuthProfile AuthProfileDto currentUserProfile, @Valid @RequestBody PostCreateRequest request){
	public GlobalApiResponse<PostCreateResponse> createPost(@Valid @RequestBody PostCreateRequest request) {

		//TODO 프로필 받고 변경하기!
		MockAuthProfileDto currentUserProfile = new MockAuthProfileDto(1L, "이름");
		PostCreateResponse response = postService.createPost(request, currentUserProfile);

		return GlobalApiResponse.<PostCreateResponse>builder()
			.code(SuccessCode.POST_CREATED.getCode())
			.message(SuccessCode.POST_CREATED.getMessage())
			.data(response)
			.build();
	}

}
