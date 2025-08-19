package org.nfactorial.newsfeed.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode implements GlobalResponseCode {
	NOT_AUTHENTICATED("AUTH-401", "인증이 필요합니다.");

	private final String code;
	private final String message;
}
