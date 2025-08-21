package org.nfactorial.newsfeed.domain.post.service;

import org.nfactorial.newsfeed.common.security.AuthProfileDto;
import org.nfactorial.newsfeed.domain.post.dto.request.PostCreateRequest;
import org.nfactorial.newsfeed.domain.post.dto.response.PostCreateResponse;
import org.nfactorial.newsfeed.domain.post.entity.Post;
import org.nfactorial.newsfeed.domain.post.repository.PostRepository;
import org.nfactorial.newsfeed.domain.profile.entity.Profile;
import org.nfactorial.newsfeed.domain.profile.service.ProfileServiceApi;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostCreationService {
    private final ProfileServiceApi profileService;
    private final PostRepository postRepository;

    @Transactional
    public PostCreateResponse save(PostCreateRequest request, AuthProfileDto currentUserProfile) {
        long profileId = currentUserProfile.profileId();
        Profile foundProfile = profileService.getProfileEntityById(profileId);

        Post savedPost = postRepository.save(Post.of(request, foundProfile));

        return PostCreateResponse.of(savedPost);
    }
}
