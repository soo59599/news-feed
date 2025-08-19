package org.nfactorial.newsfeed.common.dto;

import lombok.Builder;

@Builder
public record GlobalApiResponse<T>(
	String code,
	String message,
	T data
) {
}
