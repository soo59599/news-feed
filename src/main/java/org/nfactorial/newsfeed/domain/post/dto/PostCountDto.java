package org.nfactorial.newsfeed.domain.post.dto;

import org.nfactorial.newsfeed.common.code.ErrorCode;
import org.nfactorial.newsfeed.common.exception.BusinessException;

public record PostCountDto(
	Long profileId,
	Long postCount
) {
	// 생성자를 통한 타입 안전성 보장
	public PostCountDto {
		if (profileId == null) {
			throw new BusinessException(ErrorCode.PROFILE_NOT_FOUND);
		}
		if (postCount == null) {
			postCount = 0L;
		}
	}
}