package org.nfactorial.newsfeed.domain.interaction.repository;

import org.nfactorial.newsfeed.domain.interaction.entity.Follow;
import org.nfactorial.newsfeed.domain.profile.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {
	boolean existsByFollowerAndFollowing(Profile follower, Profile following);
}
