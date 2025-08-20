package org.nfactorial.newsfeed.domain.interaction.repository;

import org.nfactorial.newsfeed.domain.interaction.mock.ITMockLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<ITMockLike, Long> {

	boolean existsByPostIdAndProfileId(Long postId, Long profileId);
}
