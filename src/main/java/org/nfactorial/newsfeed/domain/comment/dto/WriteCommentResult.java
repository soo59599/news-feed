package org.nfactorial.newsfeed.domain.comment.dto;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record WriteCommentResult(
	long id,
	String content,
	LocalDateTime createdAt
) {
}
