package org.nfactorial.newsfeed.domain.post.repository;

import java.util.List;
import java.util.Optional;

import org.nfactorial.newsfeed.domain.post.dto.PostCountDto;
import org.nfactorial.newsfeed.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;

public interface PostRepository extends JpaRepository<Post, Long> {

	@Query("SELECT new org.nfactorial.newsfeed.domain.post.dto.PostCountDto(p.profile.id, COUNT(p)) " +
		"FROM Post p WHERE p.profile.id IN :profileIds GROUP BY p.profile.id")
	List<PostCountDto> countPostsByProfileIds(@Param("profileIds") List<Long> profileIds);

	@Query("SELECT COUNT(p) FROM Post p WHERE p.profile.id = :profileId")
	long countByProfileId(@Param("profileId") Long profileId);

	@Modifying(clearAutomatically = true)
	@Query("update Post p set p.viewCount = p.viewCount + 1 where p.id = :postId")
	void incrementViewCount(@Param("postId") Long postId);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select p from Post p where p.id = :id")
	Optional<Post> findByIdWithPessimisticLock(@Param("id") Long id);
}