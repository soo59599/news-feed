package org.nfactorial.newsfeed.domain.post.service;

import org.nfactorial.newsfeed.common.code.ErrorCode;
import org.nfactorial.newsfeed.common.exception.BusinessException;
import org.nfactorial.newsfeed.domain.comment.service.CommentServiceApi;
import org.nfactorial.newsfeed.domain.interaction.service.InteractionQueryServiceApi;
import org.nfactorial.newsfeed.domain.post.dto.response.PostGetOneResponse;
import org.nfactorial.newsfeed.domain.post.entity.Post;
import org.nfactorial.newsfeed.domain.post.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostInteractionService {
    private final PostRepository postRepository;
    private final InteractionQueryServiceApi interactionQueryService;
    private final CommentServiceApi commentService;

    @Transactional
    public PostGetOneResponse viewPost(Long postId) {

        postRepository.incrementViewCount(postId);

        Post foundPost = getPostById(postId);

        boolean hasLikedPost = interactionQueryService.hasLikedPost(foundPost.getId(), foundPost.getProfile().getId());

        int commentCount = commentService.getCommentCount(foundPost);

        return PostGetOneResponse.of(foundPost, commentCount, hasLikedPost);
    }

    private Post getPostById(long postId) {
        return postRepository.findById(postId)
            .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
    }
}
