package org.nfactorial.newsfeed.domain.comment.dto;

import org.nfactorial.newsfeed.domain.post.entity.Post;
import org.nfactorial.newsfeed.domain.profile.entity.Profile;

import lombok.Builder;

@Builder
public record WriteCommentCommand(
	Profile profile,
	Post post,
	String content) {

	public static WriteCommentCommand of(Post post, Profile profile, String content) {
		return WriteCommentCommand.builder()
			.post(post)
			.profile(profile)
			.content(content)
			.build();
	}
}
