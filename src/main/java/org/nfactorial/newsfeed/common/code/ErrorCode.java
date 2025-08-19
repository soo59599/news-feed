package org.nfactorial.newsfeed.common.code;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode implements GlobalResponseCode {
	NOT_AUTHENTICATED("AUTH-401", "인증이 필요합니다.", HttpStatus.UNAUTHORIZED),
	EMAIL_DUPLICATED("AUTH-400", "중복되는 이메일입니다.", HttpStatus.BAD_REQUEST),
	NICKNAME_DUPLICATED("AUTH-400", "중복되는 닉네임입니다.", HttpStatus.BAD_REQUEST),
	INVALID_TOKEN("AUTH-400", "유효하지 않은 토큰입니다.", HttpStatus.BAD_REQUEST),
	LOGIN_FAILED("AUTH-401-1", "로그인에 실패하였습니다.", HttpStatus.UNAUTHORIZED),
	;

	private final String code;
	private final String message;
	private final HttpStatus httpStatus;
}
