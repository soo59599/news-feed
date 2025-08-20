package org.nfactorial.newsfeed.domain.interaction.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FollowStatusResponse {

	private boolean isFollowing;

	public static FollowStatusResponse of(boolean isFollowing) {
		return new FollowStatusResponse(isFollowing);
	}
}
