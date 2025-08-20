package org.nfactorial.newsfeed.domain.comment.dto;

import lombok.Builder;

@Builder
public record WriteCommentCommand(
	long profileId,
	long postId,
	String content
) {

	public static WriteCommentCommand of(long postId, long profileId, String content) {
		return WriteCommentCommand.builder()
			.postId(postId)
			.profileId(profileId)
			.content(content)
			.build();
	}
}
