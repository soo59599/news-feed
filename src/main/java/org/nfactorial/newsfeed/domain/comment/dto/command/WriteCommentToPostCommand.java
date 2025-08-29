package org.nfactorial.newsfeed.domain.comment.dto.command;

import lombok.Builder;

@Builder
public record WriteCommentToPostCommand(
	long profileId,
	long postId,
	String content,
	Long parentId) {

	public static WriteCommentToPostCommand of(long postId, long profileId, String content, Long parentId) {
		return WriteCommentToPostCommand.builder()
			.postId(postId)
			.profileId(profileId)
			.content(content)
			.parentId(parentId)
			.build();
	}
}
