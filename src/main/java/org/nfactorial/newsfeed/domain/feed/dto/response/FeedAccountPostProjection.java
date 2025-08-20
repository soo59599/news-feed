package org.nfactorial.newsfeed.domain.feed.dto.response;

import java.time.LocalDateTime;

public interface FeedAccountPostProjection {
	Long getId();

	long getProfileId();

	String getContent();

	int getLikeCount();

	int getCommentCount();

	LocalDateTime getCreatedAt();

	String getNickname();
}
