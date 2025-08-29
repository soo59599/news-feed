package org.nfactorial.newsfeed.domain.post.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import org.nfactorial.newsfeed.domain.comment.dto.response.commentResponse;
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
	List<commentResponse> comments,
	LocalDateTime createdAt,
	LocalDateTime modifiedAt
) {
	public static PostGetOneResponse of(Post post, int commentCount, boolean hasLikedPost, List<commentResponse> topLevelComments) {
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
			.comments(topLevelComments)
			.createdAt(post.getCreatedAt())
			.modifiedAt(post.getModifiedAt())
			.build();
	}
}