package org.nfactorial.newsfeed.domain.interaction.repository;

import java.util.List;
import java.util.Optional;

import org.nfactorial.newsfeed.domain.interaction.entity.Follow;
import org.nfactorial.newsfeed.domain.profile.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FollowRepository extends JpaRepository<Follow, Long> {
	boolean existsByFollowerAndFollowing(Profile follower, Profile following);

	Optional<Follow> findByFollowerAndFollowing(Profile follower, Profile following);

	@Query("SELECT f.following.id FROM Follow f WHERE f.follower.id = :followerId")
	List<Long> findFollowingIdsByFollowerId(Long followerId);
}
