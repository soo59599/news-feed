package org.nfactorial.newsfeed.domain.post.entity;

import org.nfactorial.newsfeed.common.entity.BaseTimeEntity;
import org.nfactorial.newsfeed.domain.post.dto.request.PostCreateRequest;
import org.nfactorial.newsfeed.domain.profile.entity.Profile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "profile_id", nullable = false)
	private Profile profile;

	@Column(nullable = false)
	private String content;

	@Column(nullable = false)
	private int likeCount;

	@Column(nullable = false)
	private int viewCount;

	public static Post of(PostCreateRequest request, Profile profile) {
		Post post = new Post();
		post.content = request.content();
		post.profile = profile;
		post.likeCount = 0;
		post.viewCount = 0;
		return post;
	}

	public void updateContent(String updatedContent) {
		this.content = updatedContent;
	}

	public void incrementLikeCount() {
		this.likeCount++;
	}

	public void decrementLikeCount() {
		if (this.likeCount > 0) {
			this.likeCount--;
		}
	}
}
