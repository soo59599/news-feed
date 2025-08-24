package org.nfactorial.newsfeed.domain.auth;

public interface AuthConstants {
	public static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).+$";
	public static final String PASSWORD_FORMAT_ILLEGAL_MESSAGE = "패스워드는 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다.";
}
