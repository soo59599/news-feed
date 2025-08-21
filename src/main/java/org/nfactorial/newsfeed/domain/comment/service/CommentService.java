package org.nfactorial.newsfeed.domain.comment.service;

import java.util.List;

import org.nfactorial.newsfeed.common.code.ErrorCode;
import org.nfactorial.newsfeed.common.exception.BusinessException;
import org.nfactorial.newsfeed.domain.comment.dto.WriteCommentCommand;
import org.nfactorial.newsfeed.domain.comment.entity.Comment;
import org.nfactorial.newsfeed.domain.comment.repository.CommentRepository;
import org.nfactorial.newsfeed.domain.post.entity.Post;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	@Transactional
	@Override
	public Comment writeComment(WriteCommentCommand command) {
		Comment comment = Comment.write(command.post(), command.profile(), command.content());
		Comment savedComment = commentRepository.save(comment);
		return savedComment;
	}

	@Transactional
	public void deleteById(long commentId, long profileId) {
		Comment comment = getOwnedComment(commentId, profileId);
		commentRepository.delete(comment);
	}

	@Transactional
	public String updateById(long commentId, String content, long profileId) {
		Comment comment = getOwnedComment(commentId, profileId);
		comment.updateContent(content);
		return comment.getContent();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Comment> commentListByPost(Post post) {
		List<Comment> comments = commentRepository.findAllByPost(post);
		return comments;
	}

	private Comment getOwnedComment(long commentId, long profileId) {
		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));
		if (comment.getProfile().getId() != profileId) {
			throw new BusinessException(ErrorCode.COMMENT_NOT_YOURS);
		}
		return comment;
	}
}
