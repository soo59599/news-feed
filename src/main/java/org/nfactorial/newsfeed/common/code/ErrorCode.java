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
	ACCOUNT_NOT_FOUND("AUTH-404", "계정을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	POST_NOT_FOUND("POST-404", "해당 게시글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	POST_ACCESS_DENIED("POST-403", "게시글에 대한 권한이 없습니다.", HttpStatus.FORBIDDEN),
	PASSWORD_DOESNT_MATCH("AUTH-401-2", "비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),
	PROFILE_NOT_FOUND("PROFILE-404", "프로필을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

	// Interaction
	LIKE_ALREADY_EXISTS("INTR-409", "이미 좋아요를 누른 게시물입니다.", HttpStatus.CONFLICT),

	// Comment
	COMMENT_NOT_FOUND("COMMENT-404", "댓글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	COMMENT_NOT_YOURS("COMMENT-403", "본인의 댓글이 아닙니다.", HttpStatus.FORBIDDEN);

	private final String code;
	private final String message;
	private final HttpStatus httpStatus;
}
