package org.nfactorial.newsfeed.domain.comment;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.nfactorial.newsfeed.common.dto.GlobalApiResponse;
import org.nfactorial.newsfeed.domain.commenting.dto.response.GetCommentsFromPostResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class GetCommentE2ETest extends CommentE2ETest {

    private String accessToken;
    private Long postId;

    @BeforeEach
    void setUp() {
        String email = signUp("test@email.com", "password123!", "testuser");
        accessToken = login(email, "password123!");
        postId = createPost(accessToken, "This is a test post.");
    }

    @Test
    @DisplayName("댓글 목록 조회 성공")
    void getComments_success() {
        // given
        // 댓글 2개 작성
        writeComment(accessToken, postId, "First comment");
        writeComment(accessToken, postId, "Second comment");

        // when
        ResponseEntity<GlobalApiResponse<GetCommentsFromPostResponse>> response = restTemplate.exchange(
            "/api/v1/posts/" + postId + "/comments",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<>() {
            });

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().data().comments()).hasSize(2);
        assertThat(response.getBody().data().comments().get(0).content()).isEqualTo("First comment");
        assertThat(response.getBody().data().comments().get(1).content()).isEqualTo("Second comment");
    }

    @Test
    @DisplayName("댓글 없는 게시물 조회 시 빈 목록 반환")
    void getComments_emptyList_whenNoComments() {
        // given - no comments

        // when
        ResponseEntity<GlobalApiResponse<GetCommentsFromPostResponse>> response = restTemplate.exchange(
            "/api/v1/posts/" + postId + "/comments",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<>() {
            });

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().data().comments()).isEmpty();
    }

}