package org.nfactorial.newsfeed.domain.feed.dto.response;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class FeedResponse {

	private String nickname;
	private String contents;
	private int likeCount;
	private int commentCount;
	private LocalDateTime createdAt;

	public FeedResponse(String nickname, String contents, int likeCount, int commentCount, LocalDateTime createdAt) {
		this.nickname = nickname;
		this.contents = contents;
		this.likeCount = likeCount;
		this.commentCount = commentCount;
		this.createdAt = createdAt;
	}

	public static FeedResponse of(String nickname, String contents, int likeCount, int commentCount,
		LocalDateTime createdAt) {
		return new FeedResponse(nickname, contents, likeCount, commentCount, createdAt);
	}
}
