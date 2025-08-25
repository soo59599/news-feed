package org.nfactorial.newsfeed.domain.comment;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.nfactorial.newsfeed.common.dto.GlobalApiResponse;
import org.nfactorial.newsfeed.domain.comment.dto.request.UpdateCommentRequest;
import org.nfactorial.newsfeed.domain.comment.dto.response.UpdateCommentResponse;
import org.nfactorial.newsfeed.domain.comment.entity.Comment;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class UpdateCommentE2ETest extends CommentE2ETest {

    private String userAToken;
    private String userBToken;
    private Long postId;
    private Long commentId;

    @BeforeEach
    void setUp() {
        String userAEmail = signUp("userA@email.com", "Password123!", "userA");
        userAToken = login(userAEmail, "Password123!");

        String userBEmail = signUp("userB@email.com", "Password123!", "userB");
        userBToken = login(userBEmail, "Password123!");

        postId = createPost(userAToken, "This is a post by userA.");
        commentId = writeComment(userAToken, postId, "This is a comment by userA.");
    }

    @Test
    @DisplayName("댓글 수정 성공")
    void updateComment_success() {
        // given
        UpdateCommentRequest request = new UpdateCommentRequest("Updated content");
        headers.setBearerAuth(userAToken);

        // when
        ResponseEntity<GlobalApiResponse<UpdateCommentResponse>> response = restTemplate.exchange(
            "/api/v1/comments/" + commentId,
            HttpMethod.PATCH,
            new HttpEntity<>(request, headers),
            new ParameterizedTypeReference<>() {
            });

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(Objects.requireNonNull(response.getBody()).data().content()).isEqualTo("Updated content");

        Comment updatedComment = commentRepository.findById(commentId).orElseThrow();
        assertThat(updatedComment.getContent()).isEqualTo("Updated content");
    }

    @Test
    @DisplayName("댓글 수정 실패 - 다른 사용자의 댓글")
    void updateComment_fail_notYourComment() {
        // given
        UpdateCommentRequest request = new UpdateCommentRequest("Trying to update userA's comment");
        headers.setBearerAuth(userBToken); // userB is trying to update userA's comment

        // when
        ResponseEntity<GlobalApiResponse<Map<String, String>>> response = restTemplate.exchange(
            "/api/v1/comments/" + commentId,
            HttpMethod.PATCH,
            new HttpEntity<>(request, headers),
            new ParameterizedTypeReference<>() {
            });

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("댓글 수정 실패 - 존재하지 않는 댓글")
    void updateComment_fail_commentNotFound() {
        // given
        long nonExistentCommentId = 999L;
        UpdateCommentRequest request = new UpdateCommentRequest("Updated content");
        headers.setBearerAuth(userAToken);

        // when
        ResponseEntity<GlobalApiResponse<Map<String, String>>> response = restTemplate.exchange(
            "/api/v1/comments/" + nonExistentCommentId,
            HttpMethod.PATCH,
            new HttpEntity<>(request, headers),
            new ParameterizedTypeReference<>() {
            });

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("댓글 수정 실패 - 내용이 없는 경우")
    void updateComment_fail_emptyContent() {
        // given
        UpdateCommentRequest request = new UpdateCommentRequest(""); // Empty content
        headers.setBearerAuth(userAToken);

        // when
        ResponseEntity<GlobalApiResponse<Map<String, String>>> response = restTemplate.exchange(
            "/api/v1/comments/" + commentId,
            HttpMethod.PATCH,
            new HttpEntity<>(request, headers),
            new ParameterizedTypeReference<>() {
            });

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

}