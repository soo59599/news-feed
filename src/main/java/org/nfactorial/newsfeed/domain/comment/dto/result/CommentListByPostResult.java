package org.nfactorial.newsfeed.domain.comment.dto.result;

import java.util.List;

import org.nfactorial.newsfeed.domain.comment.entity.Comment;

public record CommentListByPostResult(
	List<SimpleComment> comments) {
	public static CommentListByPostResult of(List<Comment> comments) {
		List<SimpleComment> commentList = comments.stream()
			.map(SimpleComment::of)
			.toList();
		return new CommentListByPostResult(commentList);
	}

	public record SimpleComment(
		long id,
		String nickname,
		String content,
		List<SimpleComment> children) {
		public static SimpleComment of(Comment comment) {
			List<SimpleComment> childList = comment.getChildren().stream()
				.map(SimpleComment::of)
				.toList();
			return new SimpleComment(
				comment.getId(),
				comment.getProfile().getNickname(),
				comment.getContent(),
				childList);
		}
	}
}
