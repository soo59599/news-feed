package org.nfactorial.newsfeed.domain.interaction.repository;

import java.util.Optional;

import org.nfactorial.newsfeed.domain.interaction.entity.Like;
import org.nfactorial.newsfeed.domain.post.entity.Post;
import org.nfactorial.newsfeed.domain.profile.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LikeRepository extends JpaRepository<Like, Long> {

	Optional<Like> findByPostAndProfile(Post post, Profile profile);

	@Query("SELECT COUNT(l) > 0 FROM Like l WHERE l.post.id = :postId AND l.profile.id = :profileId")
	boolean existsByPostIdAndProfileId(Long postId, Long profileId);

	Optional<Like> findByPostIdAndProfileId(Long postId, Long profileId);
}
