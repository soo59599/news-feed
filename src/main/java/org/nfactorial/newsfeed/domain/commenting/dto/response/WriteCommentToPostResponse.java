package org.nfactorial.newsfeed.domain.commenting.dto.response;

import java.time.LocalDateTime;

import org.nfactorial.newsfeed.domain.commenting.dto.result.WriteCommentToPostResult;

import lombok.Builder;

@Builder
public record WriteCommentToPostResponse(
	long id,
	String content,
	LocalDateTime createdAt) {
	public static WriteCommentToPostResponse of(WriteCommentToPostResult result) {
		return new WriteCommentToPostResponse(result.id(), result.content(), result.createdAt());
	}
}
