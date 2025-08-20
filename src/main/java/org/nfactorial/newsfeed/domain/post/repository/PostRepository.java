package org.nfactorial.newsfeed.domain.post.repository;

import java.util.List;

import org.nfactorial.newsfeed.domain.post.dto.PostCountDto;
import org.nfactorial.newsfeed.domain.post.entity.Post;
import org.nfactorial.newsfeed.domain.profile.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

	@Query("SELECT new org.nfactorial.newsfeed.domain.post.dto.PostCountDto(p.profile.id, COUNT(p)) " +
		"FROM Post p WHERE p.profile IN :profiles GROUP BY p.profile.id")
	List<PostCountDto> countPostsByProfile(@Param("profiles") List<Profile> profiles);

}
