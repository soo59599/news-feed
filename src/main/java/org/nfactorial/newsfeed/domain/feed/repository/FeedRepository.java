package org.nfactorial.newsfeed.domain.feed.repository;

import java.time.LocalDate;
import java.util.List;

import org.nfactorial.newsfeed.domain.feed.dto.response.FeedAccountPostProjection;
import org.nfactorial.newsfeed.domain.feed.dto.response.FeedFollowPostProjection;
import org.nfactorial.newsfeed.domain.feed.dto.response.FeedResponseProjection;
import org.nfactorial.newsfeed.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FeedRepository extends JpaRepository<Post, Long> {

	@Query(value = """
			SELECT p.content, p.like_count, p.created_at,
			f.nickname, COALESCE(COUNT(c.id), 0) AS commentCount,
			p.view_count
			FROM post p
			INNER JOIN profile f ON f.id = p.profile_id
			LEFT JOIN comment c ON c.post_id = p.id
			WHERE (:startDate IS NULL OR :endDate IS NULL OR DATE(p.created_at) BETWEEN :startDate AND :endDate)
			GROUP BY p.id, p.content, p.like_count, p.created_at, f.nickname
			ORDER BY p.created_at desc
			LIMIT :size OFFSET :offset;
		""", nativeQuery = true)
	List<FeedResponseProjection> findPostWithNicknameAll(
		@Param("offset") long offset, @Param("size") long size,
		@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

	@Query(value = """
			SELECT COUNT(*) FROM post;
		""", nativeQuery = true)
	Long countPostsAll();

	@Query(value = """
			SELECT p.id, p.profile_id ,p.content, p.like_count, p.created_at,
		 	f.nickname, f.mbti, f.introduce, f.follow_count,
		 	COALESCE(COUNT(c.id), 0) AS commentCount,
		 	p.view_count
		 	FROM post p
		 	INNER JOIN profile f ON f.id = p.profile_id
		 	LEFT JOIN comment c ON c.post_id = p.id
		 	WHERE p.profile_id = :profile_id
		 	GROUP BY p.id, p.profile_id, p.content, p.like_count, p.created_at, f.nickname, f.mbti, f.introduce, f.follow_count
		 	ORDER BY p.created_at desc
			LIMIT :size OFFSET :offset;
		""", nativeQuery = true)
	List<FeedAccountPostProjection> findAccountPostAll(@Param("offset") long offset, @Param("size") long size,
		@Param("profile_id") Long profileId);

	@Query(value = """
			SELECT COUNT(*) FROM post p
		 	WHERE p.profile_id = :profile_id;
		""", nativeQuery = true)
	Long countAccountPostAll(@Param("profile_id") Long profileId);

	@Query(value = """
			SELECT p.content, p.like_count, p.created_at, f.nickname, 
			COALESCE(COUNT(c.id), 0) AS commentCount,
			p.view_count
			FROM post p
		 	INNER JOIN profile f ON f.id = p.profile_id
		 	INNER JOIN follow fw ON fw.following_id = f.id
		 	LEFT JOIN comment c ON c.post_id = p.id
		 	WHERE fw.follower_id = :follower_id
			GROUP BY p.id, p.content, p.like_count, p.created_at, f.nickname
		 	ORDER BY p.created_at DESC;
		""", nativeQuery = true)
	List<FeedFollowPostProjection> findFollowPostAll(@Param("follower_id") Long followerId);

	@Query(value = """
			SELECT COUNT(*) FROM post p
		 	INNER JOIN profile f ON f.id = p.profile_id
		  	INNER JOIN follow fw ON fw.following_id = f.id
		  	WHERE fw.follower_id = :follower_id;
		""", nativeQuery = true)
	Long countFollowPostAll(@Param("follower_id") Long followerId);
}
