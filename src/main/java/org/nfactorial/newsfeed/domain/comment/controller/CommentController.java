package org.nfactorial.newsfeed.domain.comment.controller;

import org.nfactorial.newsfeed.common.code.SuccessCode;
import org.nfactorial.newsfeed.common.dto.GlobalApiResponse;
import org.nfactorial.newsfeed.common.security.AuthProfile;
import org.nfactorial.newsfeed.common.security.AuthProfileDto;
import org.nfactorial.newsfeed.domain.comment.dto.UpdateCommentRequest;
import org.nfactorial.newsfeed.domain.comment.dto.UpdateCommentResponse;
import org.nfactorial.newsfeed.domain.comment.service.CommentService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CommentController {
	private final CommentService commentService;

	@DeleteMapping("/comments/{commentId}")
	public GlobalApiResponse<?> deleteComment(@PathVariable("commentId")
	long commentId,
		@AuthProfile
		AuthProfileDto authProfile) {
		commentService.deleteById(commentId, authProfile.profileId());
		return GlobalApiResponse.of(SuccessCode.OK, null);
	}

	@PatchMapping("/comments/{commentId}")
	public GlobalApiResponse<?> updateComment(@PathVariable("commentId")
	long commentId,
		@AuthProfile
		AuthProfileDto authProfile,
		@Valid @RequestBody
		UpdateCommentRequest request) {
		String updatedContent = commentService.updateById(commentId, request.content(), authProfile.profileId());
		return GlobalApiResponse.of(SuccessCode.OK, new UpdateCommentResponse(commentId, updatedContent));
	}
}
