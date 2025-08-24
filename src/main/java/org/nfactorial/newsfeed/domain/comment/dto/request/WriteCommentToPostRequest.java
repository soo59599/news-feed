package org.nfactorial.newsfeed.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;

public record WriteCommentToPostRequest(
	@NotBlank
	String content) {
}
