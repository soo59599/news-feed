package org.nfactorial.newsfeed.domain.interaction.repository;

import java.util.Optional;

import org.nfactorial.newsfeed.domain.interaction.entity.Like;
import org.nfactorial.newsfeed.domain.post.entity.Post;
import org.nfactorial.newsfeed.domain.profile.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {

	boolean existsByPostIdAndProfileId(Long postId, Long profileId);

	Optional<Like> findByPostAndProfile(Post post, Profile profile);
}
