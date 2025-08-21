package org.nfactorial.newsfeed.domain.postcomment.dto.result;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record WriteCommentToPostResult(
	long id,
	String content,
	LocalDateTime createdAt) {
}
