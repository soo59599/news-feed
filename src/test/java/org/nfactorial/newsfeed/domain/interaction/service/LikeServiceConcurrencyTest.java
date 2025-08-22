package org.nfactorial.newsfeed.domain.interaction.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.nfactorial.newsfeed.domain.interaction.repository.LikeRepository;
import org.nfactorial.newsfeed.domain.post.dto.request.PostCreateRequest;
import org.nfactorial.newsfeed.domain.post.entity.Post;
import org.nfactorial.newsfeed.domain.post.repository.PostRepository;
import org.nfactorial.newsfeed.domain.profile.entity.Profile;
import org.nfactorial.newsfeed.domain.profile.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LikeServiceConcurrencyTest {

	// '좋아요'를 누를 100명의 사용자 프로필을 담을 리스트
	private final List<Profile> likerProfiles = new ArrayList<>();
	@Autowired
	private LikeService likeService;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private ProfileRepository profileRepository;

	@Autowired
	private LikeRepository likeRepository;
	private Post testPost;

	@BeforeEach
	void setUp() {
		// 테스트 실행 전, 이전 데이터 정리
		likeRepository.deleteAllInBatch();
		postRepository.deleteAllInBatch();
		profileRepository.deleteAllInBatch(); // Profile도 Post나 Like와 연관이 있다면 순서를 지켜야 합니다.

		likerProfiles.clear();

		// 1. 테스트에 사용할 게시글 생성
		// 1-1. 게시글을 작성할 사용자(Profile) 생성
		Profile postOwner = profileRepository.save(new Profile("postOwner", "ENTJ", "게시글 주인"));

		// 1-2. Post.of() 메소드가 요구하는 PostCreateRequest DTO 생성
		PostCreateRequest createRequest = new PostCreateRequest("동시성 테스트 게시글");

		// 1-3. DTO와 Profile을 사용하여 Post 엔티티 생성 및 저장
		testPost = postRepository.save(Post.of(createRequest, postOwner));

		// 2. '좋아요'를 누를 100명의 각기 다른 사용자 프로필을 미리 생성하여 DB에 저장
		for (int i = 0; i < 100; i++) {
			likerProfiles.add(profileRepository.save(new Profile("liker" + i, "INFP", "좋아요 누르는 사람 " + i)));
		}
	}

	@Test
	@DisplayName("100명의 다른 사용자가 동시에 좋아요를 눌렀을 때 likeCount가 정확히 100이 되어야 한다")
	void addLike_concurrency_test() throws InterruptedException {
		// given
		int numberOfThreads = 100;
		// 32개의 스레드를 가진 스레드 풀 생성 (CPU 코어 수에 맞춰 조절 가능)
		ExecutorService executorService = Executors.newFixedThreadPool(24);
		// 모든 스레드의 작업 완료를 기다리기 위한 동기화 장치
		CountDownLatch latch = new CountDownLatch(numberOfThreads);

		// when
		// 100개의 '좋아요' 요청을 스레드 풀에 제출
		for (int i = 0; i < numberOfThreads; i++) {
			final int index = i; // 람다식 내부에서 사용하기 위해 final 변수로 복사

			executorService.submit(() -> {
				try {
					// 미리 생성해둔 각기 다른 프로필 ID를 사용하여 '좋아요' 요청
					likeService.addLike(testPost.getId(), likerProfiles.get(index).getId());
				} finally {
					latch.countDown(); // 작업이 끝나면 (성공하든 실패하든) 래치의 카운트를 1 감소
				}
			});
		}

		// 모든 스레드의 작업이 끝날 때까지(카운트가 0이 될 때까지) 메인 스레드는 대기
		latch.await();
		executorService.shutdown();

		// then
		// 모든 '좋아요' 요청이 처리된 후, 데이터베이스에서 게시글 정보를 다시 조회
		Post foundPost = postRepository.findById(testPost.getId()).orElseThrow();

		// likeCount가 스레드 수(100)와 일치하는지 확인
		assertEquals(100, foundPost.getLikeCount());
	}

	@Test
	@DisplayName("100명의 다른 사용자가 동시에 좋아요를 취소했을 때 likeCount가 0이 되어야 한다")
	void cancelLike_concurrency_test() throws InterruptedException {
		// given: 먼저 100명이 좋아요를 누름
		for (Profile profile : likerProfiles) {
			likeService.addLike(testPost.getId(), profile.getId());
		}
		assertEquals(100, postRepository.findById(testPost.getId()).orElseThrow().getLikeCount());

		int numberOfThreads = 100;
		ExecutorService executorService = Executors.newFixedThreadPool(24);
		CountDownLatch latch = new CountDownLatch(numberOfThreads);

		// when: 100명이 동시에 좋아요 취소
		for (int i = 0; i < numberOfThreads; i++) {
			final int index = i;
			executorService.submit(() -> {
				try {
					likeService.cancelLike(testPost.getId(), likerProfiles.get(index).getId());
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();
		executorService.shutdown();

		// then: likeCount가 0이어야 함
		Post foundPost = postRepository.findById(testPost.getId()).orElseThrow();
		assertEquals(0, foundPost.getLikeCount());
	}

}