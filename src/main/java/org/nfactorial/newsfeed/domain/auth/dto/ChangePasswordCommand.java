package org.nfactorial.newsfeed.domain.auth.dto;

import org.nfactorial.newsfeed.common.security.AuthProfileDto;

public record ChangePasswordCommand(
	long accountId,
	String currentPassword,
	String changePassword
) {
	public static ChangePasswordCommand of(AuthProfileDto authProfile, ChangePasswordRequest request) {
		return new ChangePasswordCommand(authProfile.accountId(), request.currentPassword(), request.changePassword());
	}
}
