package org.nfactorial.newsfeed.domain.auth.dto;

import org.nfactorial.newsfeed.domain.auth.AuthConstants;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record ChangePasswordRequest(
	@NotBlank
	String currentPassword,
	@NotBlank @Size(min = 8) @Pattern(regexp = AuthConstants.PASSWORD_PATTERN)
	String changePassword) {
}
