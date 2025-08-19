package org.nfactorial.newsfeed.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SuccessCode implements GlobalResponseCode {
	POST_CREATED("POST-002","글 작성 성공"),
	ACCOUNT_CREATED("AUTH-201", "회원가입 성공");

	private final String code;
	private final String message;
}
