package org.nfactorial.newsfeed.domain.comment.service;

import java.util.List;

import org.nfactorial.newsfeed.domain.comment.entity.Comment;
import org.nfactorial.newsfeed.domain.comment.repository.CommentRepository;
import org.nfactorial.newsfeed.domain.post.entity.Post;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService implements CommentServiceApi {
	private final CommentRepository commentRepository;

	@Override
	public int getCommentCount(Post post) {
		List<Comment> comments = commentRepository.findAllByPost(post);
		return comments.size();
	}
}
