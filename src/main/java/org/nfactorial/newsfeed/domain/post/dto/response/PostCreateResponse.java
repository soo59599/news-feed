package org.nfactorial.newsfeed.domain.post.dto.response;

import java.time.LocalDateTime;

import org.nfactorial.newsfeed.domain.post.entity.Post;
import org.nfactorial.newsfeed.domain.profile.entity.Profile;

import lombok.Builder;

@Builder
public record PostCreateResponse(
	Long postId,
	Long profileId,
	String nickname,
	String content,
	int likeCount,
	LocalDateTime createdAt,
	LocalDateTime modifiedAt
) {

	public static PostCreateResponse of(Post post) {
		Profile profile = post.getProfile();

		return PostCreateResponse.builder()
			.postId(post.getId())
			.profileId(profile.getId())
			.nickname(profile.getNickname())
			.content(post.getContent())
			.likeCount(post.getLikeCount())
			.createdAt(post.getCreatedAt())
			.modifiedAt(post.getModifiedAt())
			.build();
	}
}
