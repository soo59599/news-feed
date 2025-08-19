package org.nfactorial.newsfeed.common.dto;

import org.nfactorial.newsfeed.common.code.GlobalResponseCode;

import lombok.Builder;

@Builder
public record GlobalApiResponse<T>(
	String code,
	String message,
	T data
) {
	public static <T> GlobalApiResponse of(GlobalResponseCode code, T data) {
		return GlobalApiResponse.builder()
			.code(code.getCode())
			.message(code.getMessage())
			.data(data)
			.build();
	}
}
