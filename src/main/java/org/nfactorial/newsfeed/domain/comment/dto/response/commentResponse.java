package org.nfactorial.newsfeed.domain.comment.dto.response;

import org.nfactorial.newsfeed.domain.comment.entity.Comment;

public record commentResponse(
	long id,
	String nickname,
	String content) {
	public static commentResponse of(Comment comment) {
		return new commentResponse(
			comment.getId(),
			comment.getProfile().getNickname(),
			comment.getContent());
	}
}