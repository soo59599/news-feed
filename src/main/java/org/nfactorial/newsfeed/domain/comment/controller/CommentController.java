package org.nfactorial.newsfeed.domain.comment.controller;

import org.nfactorial.newsfeed.common.code.SuccessCode;
import org.nfactorial.newsfeed.common.dto.GlobalApiResponse;
import org.nfactorial.newsfeed.common.security.AuthProfile;
import org.nfactorial.newsfeed.common.security.AuthProfileDto;
import org.nfactorial.newsfeed.domain.comment.dto.command.WriteCommentToPostCommand;
import org.nfactorial.newsfeed.domain.comment.dto.request.UpdateCommentRequest;
import org.nfactorial.newsfeed.domain.comment.dto.request.WriteCommentToPostRequest;
import org.nfactorial.newsfeed.domain.comment.dto.response.GetCommentsFromPostResponse;
import org.nfactorial.newsfeed.domain.comment.dto.response.UpdateCommentResponse;
import org.nfactorial.newsfeed.domain.comment.dto.response.WriteCommentToPostResponse;
import org.nfactorial.newsfeed.domain.comment.dto.result.CommentListByPostResult;
import org.nfactorial.newsfeed.domain.comment.dto.result.WriteCommentToPostResult;
import org.nfactorial.newsfeed.domain.comment.service.CommentService;
import org.nfactorial.newsfeed.domain.comment.service.PostCommentingService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
	private final PostCommentingService postCommentingService;

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

	//대댓글 보기
	@GetMapping("/posts/{postId}/comments")
	public GlobalApiResponse<?> getCommentsFromPost(@PathVariable("postId")
	long postId) {
		CommentListByPostResult result = postCommentingService.commentListByPost(postId);
		return GlobalApiResponse.of(SuccessCode.OK, GetCommentsFromPostResponse.of(result));
	}

	@PostMapping("/posts/{postId}/comments")
	public GlobalApiResponse<?> writeComment(@PathVariable("postId")
	long postId,
		@AuthProfile
		AuthProfileDto authProfile,
		@Valid @RequestBody
		WriteCommentToPostRequest request) {
		WriteCommentToPostCommand command = WriteCommentToPostCommand.of(postId, authProfile.profileId(),
			request.content(), request.parentId());
		WriteCommentToPostResult result = postCommentingService.writeCommentToPost(command);
		WriteCommentToPostResponse response = WriteCommentToPostResponse.of(result);
		return GlobalApiResponse.of(SuccessCode.OK, response);
	}
}
