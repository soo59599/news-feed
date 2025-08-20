package org.nfactorial.newsfeed.domain.comment.controller;

import org.nfactorial.newsfeed.common.code.SuccessCode;
import org.nfactorial.newsfeed.common.dto.GlobalApiResponse;
import org.nfactorial.newsfeed.common.security.AuthProfile;
import org.nfactorial.newsfeed.common.security.AuthProfileDto;
import org.nfactorial.newsfeed.domain.comment.dto.WriteCommentCommand;
import org.nfactorial.newsfeed.domain.comment.dto.WriteCommentRequest;
import org.nfactorial.newsfeed.domain.comment.dto.WriteCommentResponse;
import org.nfactorial.newsfeed.domain.comment.dto.WriteCommentResult;
import org.nfactorial.newsfeed.domain.comment.service.CommentService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

	@PostMapping("/posts/{postId}/comments")
	public GlobalApiResponse<?> writeComment(@PathVariable("postId") long postId,
		@AuthProfile AuthProfileDto authProfile,
		@Valid @RequestBody WriteCommentRequest request) {
		WriteCommentCommand command = WriteCommentCommand.of(postId, authProfile.profileId(), request.content());
		WriteCommentResult result = commentService.writeComment(command);
		WriteCommentResponse response = WriteCommentResponse.of(result);
		return GlobalApiResponse.of(SuccessCode.OK, response);
	}

	@DeleteMapping("/comments/{commentId}")
	public GlobalApiResponse<?> deleteComment(@PathVariable("commentId") long commentId) {
		commentService.deleteById(commentId);
		return GlobalApiResponse.of(SuccessCode.OK, null);
	}
}
