package org.nfactorial.newsfeed.domain.post.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.nfactorial.newsfeed.domain.comment.dto.response.commentResponse;
import org.nfactorial.newsfeed.domain.comment.entity.Comment;
import org.nfactorial.newsfeed.domain.comment.service.CommentServiceApi;
import org.nfactorial.newsfeed.domain.interaction.service.InteractionQueryServiceApi;
import org.nfactorial.newsfeed.domain.post.dto.response.PostGetOneResponse;
import org.nfactorial.newsfeed.domain.post.entity.Post;
import org.nfactorial.newsfeed.domain.post.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostInteractionService {
    private final PostRepository postRepository;
    private final InteractionQueryServiceApi interactionQueryService;
    private final CommentServiceApi commentService;
    private final GetPostByIdHelper getPostByIdHelper;

    @Transactional
    public PostGetOneResponse viewPost(Long postId, HttpServletRequest request, HttpServletResponse response) {

        Post foundPost = getPostByIdHelper.execute(postId);

        // 쿠키 체크
        boolean increaseViewCount = increaseViewCount(postId, request, response);

        if (increaseViewCount) {
            // 조회수 증가 (1. DB, 2. Entity)
            postRepository.incrementViewCount(postId);
            foundPost.incrementViewCount();
        }

        boolean hasLikedPost = interactionQueryService.hasLikedPost(foundPost.getId(), foundPost.getProfile().getId());

        int commentCount = commentService.getCommentCount(foundPost);

        List<Comment> allComments = foundPost.getComments();
        List<commentResponse> commentResponses = allComments.stream()
            .filter(comment -> comment.getParent() == null)
            .map(commentResponse::of)
            .toList();

        return PostGetOneResponse.of(foundPost, commentCount, hasLikedPost, commentResponses);
    }

    /**
     * 쿠키를 확인하여 오늘 처음 조회하는지 체크
     * @param postId 게시글 ID
     * @param request HTTP 요청
     * @param response HTTP 응답
     * @return 조회수 증가 여부
     */
    private boolean increaseViewCount(Long postId, HttpServletRequest request, HttpServletResponse response) {
        String cookieName = "viewed_post_" + postId;
        String today = LocalDate.now().toString(); // yyyy-MM-dd 형식

        // 기존 쿠키 확인
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    String lastViewedDate = cookie.getValue();
                    // 오늘 이미 조회했다면 조회수 증가하지 않음
                    if (today.equals(lastViewedDate)) {
                        return false;
                    }
                }
            }
        }

        // 자정까지 남은 시간 계산
        int secondsUntilMidnight = getSecondsUntilMidnight();

        // 새로운 쿠키 설정 (자정까지만 유효)
        Cookie viewCookie = new Cookie(cookieName, today);
        viewCookie.setMaxAge(secondsUntilMidnight); // 자정까지 남은 시간
        viewCookie.setPath("/");
        viewCookie.setHttpOnly(true);
        response.addCookie(viewCookie);

        return true; // 조회수 증가해야 함
    }

    /**
     * 현재 시각부터 자정까지 남은 시간을 초 단위로 계산
     * @return 자정까지 남은 초
     */
    private int getSecondsUntilMidnight() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnight = now.toLocalDate().plusDays(1).atStartOfDay();
        return (int) Duration.between(now, midnight).getSeconds();
    }
}
