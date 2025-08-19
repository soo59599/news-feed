package org.nfactorial.newsfeed.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.nfactorial.newsfeed.domain.post.dto.request.PostCreateRequest;
import org.nfactorial.newsfeed.domain.post.dto.response.PostCreateResponse;
import org.nfactorial.newsfeed.domain.post.entity.Post;
import org.nfactorial.newsfeed.domain.post.mock.MockAuthProfileDto;
import org.nfactorial.newsfeed.domain.post.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    //TODO 프로필 받고 변경하기!
    public PostCreateResponse createPost(PostCreateRequest request, MockAuthProfileDto currentUserProfile) {
        Post savedPost = postRepository.save(Post.of(request, currentUserProfile));
        return PostCreateResponse.of(savedPost);
    }
}
