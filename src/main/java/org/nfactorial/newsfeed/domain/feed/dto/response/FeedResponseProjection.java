package org.nfactorial.newsfeed.domain.feed.dto.response;

import java.time.LocalDateTime;

public interface FeedResponseProjection {
	String getContent();

	int getLikeCount();

	Integer getCommentCount();

	LocalDateTime getCreatedAt();

	String getNickname();

	Integer getViewCount();
}
