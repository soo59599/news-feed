package org.nfactorial.newsfeed.domain.commenting.dto.request;

import jakarta.validation.constraints.NotBlank;

public record WriteCommentToPostRequest(
	@NotBlank
	String content) {
}
