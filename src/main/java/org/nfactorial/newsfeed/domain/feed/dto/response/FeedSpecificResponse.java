package org.nfactorial.newsfeed.domain.feed.dto.response;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class FeedSpecificResponse {

	private String nickname;
	private LocalDateTime createdAt;
	private String content;
	private int likeCount;
	private int commentCount;
	private int viewCount;

	public FeedSpecificResponse(String nickname, LocalDateTime createdAt, String content, int likeCount,
		int commentCount,
		int viewCount) {
		this.nickname = nickname;
		this.createdAt = createdAt;
		this.content = content;
		this.likeCount = likeCount;
		this.commentCount = commentCount;
		this.viewCount = viewCount;
	}

	public static FeedSpecificResponse of(String nickname, LocalDateTime createdAt, String content, int likeCount,
		int commentCount, int viewCount) {
		return new FeedSpecificResponse(nickname, createdAt, content, likeCount, commentCount, viewCount);
	}
}
