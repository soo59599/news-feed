package org.nfactorial.newsfeed.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;

public record WriteCommentRequest(
	@NotBlank
	String content
) {
}
