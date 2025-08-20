package org.nfactorial.newsfeed.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ChangePasswordRequest(
	@NotBlank
	String currentPassword,
	@NotBlank
	String changePassword
) {
}
