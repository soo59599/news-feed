package org.nfactorial.newsfeed.domain.comment.dto.result;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record WriteCommentToPostResult(
	long id,
	String content,
	LocalDateTime createdAt) {
}
