package org.nfactorial.newsfeed.domain.feed.dto.response;

import lombok.Getter;

@Getter
public class FeedProfileInfoResponse {
	private String nickname;
	private int followCount;
	private Long postCount;
	private String mbti;
	private String introduce;

	public FeedProfileInfoResponse(String nickname, int followCount, Long postCount, String mbti, String introduce) {
		this.nickname = nickname;
		this.followCount = followCount;
		this.postCount = postCount;
		this.mbti = mbti;
		this.introduce = introduce;
	}

	public static FeedProfileInfoResponse of(String nickname, int followCount, Long postCount, String mbti,
		String introduce) {
		return new FeedProfileInfoResponse(nickname, followCount, postCount, mbti, introduce);
	}
}
