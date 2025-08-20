package org.nfactorial.newsfeed.domain.interaction.controller;

import org.nfactorial.newsfeed.common.code.SuccessCode;
import org.nfactorial.newsfeed.common.dto.GlobalApiResponse;
import org.nfactorial.newsfeed.domain.interaction.mock.ITAuthProfileDto;
import org.nfactorial.newsfeed.domain.interaction.service.InteractionService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class InteractionController {

	private final InteractionService interactionService;

	@PostMapping("/api/v1/posts/{postId}/likes")
	// public GlobalApiResponse<Void> addLike(@PathVariable Long postId, @AuthProfile AuthProfileDto currentProfileDto) {
	public GlobalApiResponse<Void> addLike(@PathVariable Long postId) {

		// 로그인 상태 확인 및 사용자 정보 가져오기
		// TODO: annotation을 통한 profile 엔티티 반환 시 삭제 예정, 영속성 컨텍스트를 위해 임시 사용
		ITAuthProfileDto currentProfileDto = new ITAuthProfileDto(1L, 1L);

		interactionService.addLike(postId, currentProfileDto.profileId());
		return new GlobalApiResponse<>(SuccessCode.CREATED.getCode(), SuccessCode.CREATED.getMessage(), null);
	}
}
