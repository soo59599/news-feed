package org.nfactorial.newsfeed.domain.feed.dto.response;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class FeedFollowPostResponse {

	private String nickname;
	private String contents;
	private int likeCount;
	private int commentCount;
	private LocalDateTime createdAt;
	private int viewCount;

	public FeedFollowPostResponse(String nickname, String contents, int likeCount, int commentCount,
		LocalDateTime createdAt, int viewCount) {
		this.nickname = nickname;
		this.contents = contents;
		this.likeCount = likeCount;
		this.commentCount = commentCount;
		this.createdAt = createdAt;
		this.viewCount = viewCount;
	}

	public static FeedFollowPostResponse of(String nickname, String contents, int likeCount, int commentCount,
		LocalDateTime createdAt, int viewCount) {
		return new FeedFollowPostResponse(nickname, contents, likeCount, commentCount, createdAt, viewCount);
	}
}
