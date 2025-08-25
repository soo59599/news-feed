package org.nfactorial.newsfeed.domain.profile.dto;

import org.nfactorial.newsfeed.domain.profile.entity.Profile;

import lombok.Builder;

@Builder
public record ProfileSummaryDto(
	long id,
	String nickname,
	String introduce,
	String mbti,
	int followCount,
	long postCount
) {
	public static ProfileSummaryDto of(Profile profile, long postCount) {
		return ProfileSummaryDto.builder()
			.id(profile.getId())
			.nickname(profile.getNickname())
			.introduce(profile.getIntroduce())
			.mbti(profile.getMbti())
			.followCount(profile.getFollowCount())
			.postCount(postCount)
			.build();
	}
}

