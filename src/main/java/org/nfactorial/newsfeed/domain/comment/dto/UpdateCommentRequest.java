package org.nfactorial.newsfeed.domain.comment.dto;

import lombok.Builder;

@Builder
public record UpdateCommentRequest(
	String content
) {
}
