package org.nfactorial.newsfeed.domain.comment.service;

import java.util.List;

import org.nfactorial.newsfeed.domain.comment.dto.command.WriteCommentToPostCommand;
import org.nfactorial.newsfeed.domain.comment.entity.Comment;
import org.nfactorial.newsfeed.domain.comment.repository.CommentRepository;
import org.nfactorial.newsfeed.domain.comment.dto.result.CommentListByPostResult;
import org.nfactorial.newsfeed.domain.comment.dto.result.WriteCommentToPostResult;
import org.nfactorial.newsfeed.domain.post.entity.Post;
import org.nfactorial.newsfeed.domain.post.service.PostServiceApi;
import org.nfactorial.newsfeed.domain.profile.entity.Profile;
import org.nfactorial.newsfeed.domain.profile.service.ProfileServiceApi;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostCommentingService {
    private final PostServiceApi postService;
    private final ProfileServiceApi profileService;
    private final CommentRepository commentRepository;

    @Transactional
    public WriteCommentToPostResult writeCommentToPost(WriteCommentToPostCommand command) {
        Post post = postService.getPostById(command.postId());
        Profile profile = profileService.getProfileEntityById(command.profileId());
        Comment comment = Comment.write(post, profile, command.content());
        Comment savedComment = commentRepository.save(comment);
        return WriteCommentToPostResult.builder()
            .id(savedComment.getId())
            .createdAt(savedComment.getCreatedAt())
            .content(savedComment.getContent())
            .build();
    }

    @Transactional(readOnly = true)
    public CommentListByPostResult commentListByPost(long postId) {
        Post post = postService.getPostById(postId);
        List<Comment> comments = commentRepository.findAllByPost(post);
        return CommentListByPostResult.of(comments);
    }
}
