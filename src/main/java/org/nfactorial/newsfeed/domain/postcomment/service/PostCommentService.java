package org.nfactorial.newsfeed.domain.postcomment.service;

import java.util.List;

import org.nfactorial.newsfeed.domain.comment.dto.WriteCommentCommand;
import org.nfactorial.newsfeed.domain.comment.entity.Comment;
import org.nfactorial.newsfeed.domain.comment.service.CommentServiceApi;
import org.nfactorial.newsfeed.domain.post.entity.Post;
import org.nfactorial.newsfeed.domain.post.service.PostServiceApi;
import org.nfactorial.newsfeed.domain.postcomment.dto.command.WriteCommentToPostCommand;
import org.nfactorial.newsfeed.domain.postcomment.dto.result.CommentListByPostResult;
import org.nfactorial.newsfeed.domain.postcomment.dto.result.WriteCommentToPostResult;
import org.nfactorial.newsfeed.domain.profile.entity.Profile;
import org.nfactorial.newsfeed.domain.profile.service.ProfileServiceApi;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostCommentService {
    private final PostServiceApi postService;
    private final ProfileServiceApi profileService;
    private final CommentServiceApi commentService;

    @Transactional
    public WriteCommentToPostResult writeCommentToPost(WriteCommentToPostCommand command) {
        Post post = postService.getPostById(command.postId());
        Profile profile = profileService.getProfileById(command.profileId());
        Comment savedComment = commentService.writeComment(WriteCommentCommand.of(post, profile, command.content()));
        return WriteCommentToPostResult.builder()
            .id(savedComment.getId())
            .createdAt(savedComment.getCreatedAt())
            .content(savedComment.getContent())
            .build();
    }

    @Transactional(readOnly = true)
    public CommentListByPostResult commentListByPost(long postId) {
        Post post = postService.getPostById(postId);
        List<Comment> comments = commentService.commentListByPost(post);
        return CommentListByPostResult.of(comments);
    }
}
