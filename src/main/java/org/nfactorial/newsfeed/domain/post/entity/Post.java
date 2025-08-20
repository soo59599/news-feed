package org.nfactorial.newsfeed.domain.post.entity;

import org.nfactorial.newsfeed.common.entity.BaseTimeEntity;
import org.nfactorial.newsfeed.domain.post.dto.request.PostCreateRequest;
import org.nfactorial.newsfeed.domain.post.dto.request.PostUpdateRequest;
import org.nfactorial.newsfeed.domain.post.mock.MockAuthProfileDto;
import org.nfactorial.newsfeed.domain.profile.entity.Profile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
	private Profile profile;

	@Column(nullable = false)
	private String content;

	private int likeCount;

	public static Post of(PostCreateRequest request, Profile profile) {
		Post post = new Post();
		post.content = request.content();
		post.profile = profile;
		post.likeCount = 0;
		return post;
	}

	//TODO 프로필 받고 지우기
	public static Post of(PostCreateRequest request, MockAuthProfileDto profile) {
		Post post = new Post();
		post.content = request.content();
		post.likeCount = 0;
		return post;
	}

	public void updateContent(PostUpdateRequest request) {
		this.content = request.content();
	}
}
