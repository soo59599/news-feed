package org.nfactorial.newsfeed.domain.post.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PostUpdateRequest(
	@NotBlank(message = "내용은 필수입니다.")
	String content
) {
}
