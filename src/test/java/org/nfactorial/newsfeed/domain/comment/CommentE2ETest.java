package org.nfactorial.newsfeed.domain.comment;

import java.util.Objects;

import org.junit.jupiter.api.AfterEach;
import org.nfactorial.newsfeed.domain.auth.AuthE2ETest;
import org.nfactorial.newsfeed.common.dto.GlobalApiResponse;
import org.nfactorial.newsfeed.domain.auth.dto.LoginRequest;
import org.nfactorial.newsfeed.domain.auth.dto.LoginResponse;
import org.nfactorial.newsfeed.domain.auth.dto.SignUpRequest;
import org.nfactorial.newsfeed.domain.comment.repository.CommentRepository;
import org.nfactorial.newsfeed.domain.comment.dto.request.WriteCommentToPostRequest;
import org.nfactorial.newsfeed.domain.comment.dto.response.WriteCommentToPostResponse;
import org.nfactorial.newsfeed.domain.post.dto.request.PostCreateRequest;
import org.nfactorial.newsfeed.domain.post.dto.response.PostCreateResponse;
import org.nfactorial.newsfeed.domain.post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public class CommentE2ETest extends AuthE2ETest {

    @Autowired
    protected PostRepository postRepository;

    @Autowired
    protected CommentRepository commentRepository;

    protected HttpHeaders headers;

    protected String signUp(String email, String password, String nickname) {
        SignUpRequest request = new SignUpRequest(email, password, nickname, "hi", "INFP");
        restTemplate.postForEntity("/api/v1/auth/signup", request, String.class);
        return email;
    }

    protected String login(String email, String password) {
        LoginRequest request = new LoginRequest(email, password);
        ResponseEntity<GlobalApiResponse<LoginResponse>> response = restTemplate.exchange(
            "/api/v1/auth/login",
            HttpMethod.POST,
            new HttpEntity<>(request),
            new ParameterizedTypeReference<>() {
            });
        return Objects.requireNonNull(response.getBody()).data().accessToken();
    }

    protected Long createPost(String accessToken, String content) {
        headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        PostCreateRequest request = new PostCreateRequest(content);

        ResponseEntity<GlobalApiResponse<PostCreateResponse>> response = restTemplate.exchange(
            "/api/v1/posts",
            HttpMethod.POST,
            new HttpEntity<>(request, headers),
            new ParameterizedTypeReference<>() {
            });
        return Objects.requireNonNull(response.getBody()).data().postId();
    }

    protected Long writeComment(String accessToken, Long postId, String content) {
        WriteCommentToPostRequest request = new WriteCommentToPostRequest(content);
        headers.setBearerAuth(accessToken);
        ResponseEntity<GlobalApiResponse<WriteCommentToPostResponse>> response = restTemplate.exchange(
            "/api/v1/posts/" + postId + "/comments",
            HttpMethod.POST,
            new HttpEntity<>(request, headers),
            new ParameterizedTypeReference<>() {
            });
        return Objects.requireNonNull(response.getBody()).data().id();
    }

    @AfterEach
    void tearDownComment() {
        commentRepository.deleteAll();
        postRepository.deleteAll();
    }
}