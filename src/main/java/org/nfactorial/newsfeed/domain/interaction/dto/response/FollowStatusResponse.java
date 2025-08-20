package org.nfactorial.newsfeed.domain.interaction.dto.response;

public record FollowStatusResponse(boolean isFollowing) {

	public static FollowStatusResponse of(boolean isFollowing) {
		return new FollowStatusResponse(isFollowing);
	}
}
