package org.nfactorial.newsfeed.common.dto;

import org.nfactorial.newsfeed.common.code.GlobalResponseCode;

import lombok.Builder;

@Builder
public record GlobalApiResponse<T>(
	GlobalResponseCode code,
	String message,
	T data
) {
}
