package org.nfactorial.newsfeed.domain.comment.service;

import java.util.List;

import org.nfactorial.newsfeed.domain.comment.dto.WriteCommentCommand;
import org.nfactorial.newsfeed.domain.comment.entity.Comment;
import org.nfactorial.newsfeed.domain.post.entity.Post;

public interface CommentServiceApi {
	int getCommentCount(Post post);

	List<Comment> commentListByPost(Post post);

	Comment writeComment(WriteCommentCommand command);
}
