package org.nfactorial.newsfeed.domain.feed.dto.response;

import java.time.LocalDateTime;

public interface FeedFollowPostProjection {

	String getNickname();

	String getContents();

	int getLikeCount();

	int getCommentCount();

	LocalDateTime getCreatedAt();
}
