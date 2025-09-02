package org.nfactorial.newsfeed.domain.post;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.nfactorial.newsfeed.domain.comment.service.CommentServiceApi;
import org.nfactorial.newsfeed.domain.interaction.service.InteractionQueryServiceApi;
import org.nfactorial.newsfeed.domain.post.dto.request.PostCreateRequest;
import org.nfactorial.newsfeed.domain.post.entity.Post;
import org.nfactorial.newsfeed.domain.post.repository.PostRepository;
import org.nfactorial.newsfeed.domain.post.service.GetPostByIdHelper;
import org.nfactorial.newsfeed.domain.post.service.PostInteractionService;
import org.nfactorial.newsfeed.domain.profile.entity.Profile;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;

import jakarta.servlet.http.Cookie;

@ExtendWith(MockitoExtension.class)
class PostInteractionServiceTest {

	@Mock
	private PostRepository postRepository;

	@Mock
	private GetPostByIdHelper getPostByIdHelper;

	@Mock
	private CommentServiceApi commentService;

	@Mock
	private InteractionQueryServiceApi interactionQueryService;

	@InjectMocks
	private PostInteractionService postInteractionService;

	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	private Long postId;
	private Post post;

	@BeforeEach
	void setUp() {
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		postId = 1L;

		PostCreateRequest pcRequest = new PostCreateRequest("글내용");
		Profile profile = new Profile("닉네임", "mbti", "설명");
		post = Post.of(pcRequest, profile);
		ReflectionTestUtils.setField(post, "id", postId);

		given(getPostByIdHelper.execute(postId)).willReturn(post);
	}

	@Test
	void 처음_조회하면_조회수_증가하고_쿠키_생성() {
		postInteractionService.viewPost(postId, request, response);

		verify(postRepository).incrementViewCount(postId);

		Cookie[] cookies = response.getCookies();
		assertThat(cookies).hasSize(1);
		assertThat(cookies[0].getName()).isEqualTo(getViewCookieName(postId));
	}

	@Test
	void 하루_내_재조회시_조회수_증가하지_않음() {
		String cookieName = getViewCookieName(postId);
		Cookie existingCookie = new Cookie(cookieName, LocalDate.now().toString());
		request.setCookies(existingCookie);

		postInteractionService.viewPost(postId, request, response);

		verify(postRepository, never()).incrementViewCount(postId);
		assertThat(response.getCookies()).isEmpty();
	}

	private String getViewCookieName(Long postId) {
		return "viewed_post_" + postId;
	}
}
