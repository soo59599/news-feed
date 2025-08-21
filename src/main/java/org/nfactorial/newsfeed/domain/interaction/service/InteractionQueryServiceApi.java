package org.nfactorial.newsfeed.domain.interaction.service;

import java.util.List;

import org.nfactorial.newsfeed.domain.interaction.dto.response.FollowStatusResponse;
import org.nfactorial.newsfeed.domain.profile.dto.ProfileSummaryDto;

public interface InteractionQueryServiceApi {

	FollowStatusResponse checkFollowStatus(Long followerId, Long followingId);

	List<ProfileSummaryDto> getFollowingProfiles(Long followerId);

	boolean hasLikedPost(Long postId, Long profileId);
}
