package org.nfactorial.newsfeed.domain.post.dto.response;

import org.nfactorial.newsfeed.domain.post.entity.Post;
import org.nfactorial.newsfeed.domain.profile.entity.Profile;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record PostCreateResponse(
	Long id,
	Profile profile,
	String content,
	int likeCount,
	LocalDateTime createdAt,
	LocalDateTime modifiedAt
) {

	public static PostCreateResponse of(Post post) {
		return PostCreateResponse.builder()
			.id(post.getId())
			.profile(post.getProfile())
			.content(post.getContent())
			.likeCount(post.getLikeCount())
			.createdAt(post.getCreatedAt())
			.modifiedAt(post.getModifiedAt())
			.build();
	}
}
