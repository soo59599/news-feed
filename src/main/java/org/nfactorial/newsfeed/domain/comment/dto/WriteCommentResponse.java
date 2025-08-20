package org.nfactorial.newsfeed.domain.comment.dto;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record WriteCommentResponse(
	long id,
	String content,
	LocalDateTime createdAt
) {
	public static WriteCommentResponse of(WriteCommentResult result) {
		return new WriteCommentResponse(result.id(), result.content(), result.createdAt());
	}
}
