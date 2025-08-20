package org.nfactorial.newsfeed.domain.comment.dto;

import java.util.List;

public record GetCommentsFromPostResponse(
	List<SimpleComment> comments
) {
	public static GetCommentsFromPostResponse of(CommentListByPostResult comments) {
		List<SimpleComment> commentList = comments.comments().stream()
			.map(SimpleComment::of)
			.toList();
		return new GetCommentsFromPostResponse(commentList);
	}

	record SimpleComment(
		long id,
		String nickname,
		String content
	) {
		public static SimpleComment of(CommentListByPostResult.SimpleComment comment) {
			return new SimpleComment(
				comment.id(),
				comment.nickname(),
				comment.content()
			);
		}
	}
}
