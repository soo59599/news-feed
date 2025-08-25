package org.nfactorial.newsfeed.domain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record LoginRequest(
	@NotBlank
	@Email
	String email,
	@NotBlank
	String password
) {
}
