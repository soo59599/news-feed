package org.nfactorial.newsfeed.domain.feed.repository;

import java.util.List;

import org.nfactorial.newsfeed.domain.feed.dto.response.FeedResponseProjection;
import org.nfactorial.newsfeed.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FeedRepository extends JpaRepository<Post, Long> {

	@Query(value = """
			SELECT p.content, p.like_count, p.created_at,
			f.nickname, COALESCE(COUNT(c.id), 0) AS commentCount,
			p.like_count FROM post p
			INNER JOIN profile f ON f.id = p.profile_id
			LEFT JOIN comment c ON c.post_id = p.id
			GROUP BY p.id, p.content, p.like_count, p.created_at, f.nickname
			ORDER BY p.created_at desc
			LIMIT :size OFFSET :offset;
		""", nativeQuery = true)
	List<FeedResponseProjection> findPostWithNicknameAll(@Param("offset") long offset, @Param("size") long size);

	@Query(value = """
			SELECT COUNT(*) FROM post;
		""", nativeQuery = true)
	Long countPostsAll();
}
