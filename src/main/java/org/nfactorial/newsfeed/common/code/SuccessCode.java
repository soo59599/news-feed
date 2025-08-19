package org.nfactorial.newsfeed.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SuccessCode implements GlobalResponseCode {
	CREATED("SUCC-201", "리소스 생성 성공"),
	OK("SUCC-200", "요청 성공"),
	;

	private final String code;
	private final String message;
}
