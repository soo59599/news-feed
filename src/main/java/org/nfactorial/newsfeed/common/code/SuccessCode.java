package org.nfactorial.newsfeed.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SuccessCode implements GlobalResponseCode {
	ACCOUNT_CREATED("AUTH-201", "회원가입 성공"),
	LOGIN_SUCCESS("AUTH-200", "로그인 성공"),
	POST_CREATED("POST-002","글 작성 성공");

	private final String code;
	private final String message;
}
