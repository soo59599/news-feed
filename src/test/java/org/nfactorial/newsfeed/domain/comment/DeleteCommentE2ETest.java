package org.nfactorial.newsfeed.domain.comment;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.nfactorial.newsfeed.common.dto.GlobalApiResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class DeleteCommentE2ETest extends CommentE2ETest {

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
    @DisplayName("댓글 삭제 성공")
    void deleteComment_success() {
        // given
        headers.setBearerAuth(userAToken);

        // when
        ResponseEntity<GlobalApiResponse<Void>> response = restTemplate.exchange(
            "/api/v1/comments/" + commentId,
            HttpMethod.DELETE,
            new HttpEntity<>(headers),
            new ParameterizedTypeReference<>() {
            });

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(commentRepository.findById(commentId)).isEmpty();
    }

    @Test
    @DisplayName("댓글 삭제 실패 - 다른 사용자의 댓글")
    void deleteComment_fail_notYourComment() {
        // given
        headers.setBearerAuth(userBToken); // userB is trying to delete userA's comment

        // when
        ResponseEntity<GlobalApiResponse<Map<String, String>>> response = restTemplate.exchange(
            "/api/v1/comments/" + commentId,
            HttpMethod.DELETE,
            new HttpEntity<>(headers),
            new ParameterizedTypeReference<>() {
            });

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(commentRepository.existsById(commentId)).isTrue();
    }

    @Test
    @DisplayName("댓글 삭제 실패 - 존재하지 않는 댓글")
    void deleteComment_fail_commentNotFound() {
        // given
        long nonExistentCommentId = 999L;
        headers.setBearerAuth(userAToken);

        // when
        ResponseEntity<GlobalApiResponse<Map<String, String>>> response = restTemplate.exchange(
            "/api/v1/comments/" + nonExistentCommentId,
            HttpMethod.DELETE,
            new HttpEntity<>(headers),
            new ParameterizedTypeReference<>() {
            });

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

}