package org.nfactorial.newsfeed.domain.comment.entity;

import java.util.ArrayList;
import java.util.List;

import org.nfactorial.newsfeed.common.code.ErrorCode;
import org.nfactorial.newsfeed.common.entity.BaseTimeEntity;
import org.nfactorial.newsfeed.common.exception.BusinessException;
import org.nfactorial.newsfeed.domain.post.entity.Post;
import org.nfactorial.newsfeed.domain.profile.entity.Profile;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Comment extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(nullable = false)
	private Post post;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(nullable = false)
	private Profile profile;

	@Column(nullable = false)
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	private Comment parent; // 부모 댓글

	@OneToMany(
		mappedBy = "parent",
		fetch = FetchType.LAZY,
		cascade = CascadeType.ALL,
		orphanRemoval = true)
	private List<Comment> children = new ArrayList<>(); //자식 댓글

	@Column(nullable = false)
	private int depth = 0;

	public static Comment write(Post post, Profile profile, String content) {
		Comment comment = new Comment();
		comment.post = post;
		comment.profile = profile;
		comment.content = content;
		comment.parent = null;
		return comment;
	}

	public void updateContent(String content) {
		this.content = content;
	}

	public void setParent(Comment parent) {
		if(parent.depth>=1){
			throw new BusinessException(ErrorCode.COMMENT_INVALID_PARENT);
		}
		this.parent = parent;
		parent.children.add(this);
		this.depth = parent.depth + 1;
	}
}
