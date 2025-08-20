package org.nfactorial.newsfeed.domain.comment.service;

import java.util.List;

import org.nfactorial.newsfeed.domain.comment.dto.WriteCommentCommand;
import org.nfactorial.newsfeed.domain.comment.dto.WriteCommentResult;
import org.nfactorial.newsfeed.domain.comment.entity.Comment;
import org.nfactorial.newsfeed.domain.comment.repository.CommentRepository;
import org.nfactorial.newsfeed.domain.post.entity.Post;
import org.nfactorial.newsfeed.domain.post.service.PostServiceApi;
import org.nfactorial.newsfeed.domain.profile.entity.Profile;
import org.nfactorial.newsfeed.domain.profile.service.ProfileServiceApi;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService implements CommentServiceApi {
	private final CommentRepository commentRepository;
	private final PostServiceApi postService;
	private final ProfileServiceApi profileService;

	@Override
	public int getCommentCount(Post post) {
		List<Comment> comments = commentRepository.findAllByPost(post);
		return comments.size();
	}

	@Transactional
	public WriteCommentResult writeComment(WriteCommentCommand command) {
		Post post = postService.getPostById(command.postId());
		Profile profile = profileService.getProfileById(command.profileId());
		Comment comment = Comment.write(post, profile, command.content());
		Comment savedComment = commentRepository.save(comment);
		return WriteCommentResult.builder()
			.id(savedComment.getId())
			.createdAt(savedComment.getCreatedAt())
			.content(savedComment.getContent())
			.build();
	}
}
