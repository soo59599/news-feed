package org.nfactorial.newsfeed.domain.comment.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import org.nfactorial.newsfeed.domain.comment.dto.result.CommentListByPostResult;

public record GetCommentsFromPostResponse(
	List<SimpleComment> comments) {
	public static GetCommentsFromPostResponse of(CommentListByPostResult comments) {
		List<SimpleComment> commentList = comments.comments().stream()
			.map(SimpleComment::of)
			.toList();
		return new GetCommentsFromPostResponse(commentList);
	}

	public record SimpleComment(
		long id,
		String nickname,
		String content,
		List<SimpleComment> children
	) {
		public static SimpleComment of(CommentListByPostResult.SimpleComment comment) {
			List<SimpleComment> childList = comment.children().stream().map(SimpleComment::of).toList();
			return new SimpleComment(
				comment.id(),
				comment.nickname(),
				comment.content(),
				childList);
		}
	}
}
