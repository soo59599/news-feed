package org.nfactorial.newsfeed.domain.post;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.nfactorial.newsfeed.common.dto.GlobalApiResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@DisplayName("댓글 달린 게시글 삭제 E2E 테스트")
class DeletePostWithCommentE2ETest extends PostE2ETest {

    private String userAToken;
    private Long postId;
    private Long commentId;

    @BeforeEach
    void setUp() {
        String userAEmail = signUp("userA@email.com", "Password123!", "userA");
        userAToken = login(userAEmail, "Password123!");

        postId = createPost(userAToken, "This is a post by userA.");
        commentId = writeComment(userAToken, postId, "This is a comment on userA's post.");
    }

    @Test
    @DisplayName("댓글이 달린 게시물을 삭제하면 게시물과 댓글 모두 삭제된다")
    void deletePostWithComment_success() {
        // given
        headers.setBearerAuth(userAToken);

        // when
        ResponseEntity<GlobalApiResponse<Void>> response = restTemplate.exchange(
            "/api/v1/posts/" + postId,
            HttpMethod.DELETE,
            new HttpEntity<>(headers),
            new ParameterizedTypeReference<>() {
            });

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(postRepository.findById(postId)).isEmpty();
        assertThat(commentRepository.findById(commentId)).isEmpty();
    }
}
