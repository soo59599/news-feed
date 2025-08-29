package org.nfactorial.newsfeed.domain.post.service;

import java.io.IOException;
import java.util.List;

import org.nfactorial.newsfeed.common.code.ErrorCode;
import org.nfactorial.newsfeed.common.exception.BusinessException;
import org.nfactorial.newsfeed.common.security.AuthProfileDto;
import org.nfactorial.newsfeed.domain.file.entity.File;
import org.nfactorial.newsfeed.domain.file.service.FileUploadService;
import org.nfactorial.newsfeed.domain.post.dto.request.PostCreateRequest;
import org.nfactorial.newsfeed.domain.post.dto.response.PostCreateResponse;
import org.nfactorial.newsfeed.domain.post.entity.Post;
import org.nfactorial.newsfeed.domain.post.repository.PostRepository;
import org.nfactorial.newsfeed.domain.profile.entity.Profile;
import org.nfactorial.newsfeed.domain.profile.service.ProfileServiceApi;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostCreationService {
    private final ProfileServiceApi profileService;
    private final PostRepository postRepository;
    private final FileUploadService fileUploadService;

    @Transactional
    public PostCreateResponse save(PostCreateRequest request, List<MultipartFile> files, AuthProfileDto currentUserProfile) {
        long profileId = currentUserProfile.profileId();
        Profile foundProfile = profileService.getProfileEntityById(profileId);

        Post post = Post.of(request, foundProfile);
        
        if(files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                try {
                    File result = fileUploadService.saveFile(file);
                    post.addFile(result);
                }catch (IOException e){
                    throw new BusinessException(ErrorCode.FILE_FAIL_UPLOAD);
                }
            }
        }

        Post savedPost = postRepository.save(post);

        return PostCreateResponse.of(savedPost);
    }
}
