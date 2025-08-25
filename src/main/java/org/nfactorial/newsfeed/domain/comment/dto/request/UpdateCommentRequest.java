package org.nfactorial.newsfeed.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UpdateCommentRequest(
	@NotBlank
	String content) {
}
