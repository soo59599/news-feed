package org.nfactorial.newsfeed.domain.commenting.dto.command;

import lombok.Builder;

@Builder
public record WriteCommentToPostCommand(
	long profileId,
	long postId,
	String content) {

	public static WriteCommentToPostCommand of(long postId, long profileId, String content) {
		return WriteCommentToPostCommand.builder()
			.postId(postId)
			.profileId(profileId)
			.content(content)
			.build();
	}
}
