package org.nfactorial.newsfeed.domain.comment.service;

import org.nfactorial.newsfeed.domain.post.entity.Post;

public interface CommentServiceApi {
	int getCommentCount(Post post);
}
