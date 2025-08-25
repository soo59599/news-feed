package org.nfactorial.newsfeed.domain.post.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PostCreateRequest(
	@NotBlank(message = "내용은 필수입니다.")
	String content
) {

}