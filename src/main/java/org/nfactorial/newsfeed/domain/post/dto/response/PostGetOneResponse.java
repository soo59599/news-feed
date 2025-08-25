package org.nfactorial.newsfeed.domain.post.dto.response;

import java.time.LocalDateTime;

import org.nfactorial.newsfeed.domain.post.entity.Post;
import org.nfactorial.newsfeed.domain.profile.entity.Profile;

import lombok.Builder;

@Builder
public record PostGetOneResponse(
	Long postId,
	Long profileId,
	String nickname,
	String content,
	int likeCount,
	int commentCount,
	int viewCount,
	boolean hasLikedPost,
	LocalDateTime createdAt,
	LocalDateTime modifiedAt
) {
	public static PostGetOneResponse of(Post post, int commentCount, boolean hasLikedPost) {
		Profile profile = post.getProfile();

		return PostGetOneResponse.builder()
			.postId(post.getId())
			.profileId(profile.getId())
			.nickname(profile.getNickname())
			.content(post.getContent())
			.likeCount(post.getLikeCount())
			.commentCount(commentCount)
			.viewCount(post.getViewCount())
			.hasLikedPost(hasLikedPost)
			.createdAt(post.getCreatedAt())
			.modifiedAt(post.getModifiedAt())
			.build();
	}
}