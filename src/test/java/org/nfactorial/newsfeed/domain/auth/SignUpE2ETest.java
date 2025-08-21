package org.nfactorial.newsfeed.domain.auth;

import static org.assertj.core.api.Assertions.*;

import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.nfactorial.newsfeed.common.dto.GlobalApiResponse;
import org.nfactorial.newsfeed.domain.auth.dto.SignUpRequest;
import org.nfactorial.newsfeed.domain.auth.dto.SignUpResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class SignUpE2ETest extends AuthE2ETest {

    @Test
    @DisplayName("회원가입 성공")
    void signUp_success() {
        // given
        SignUpRequest request = new SignUpRequest(
            "test@email.com",
            "Password123!",
            "testuser",
            "안녕하세요",
            "INFP");

        // when
        ResponseEntity<GlobalApiResponse<SignUpResponse>> response = restTemplate.exchange(
            "/api/v1/auth/signup",
            HttpMethod.POST,
            new HttpEntity<>(request),
            new ParameterizedTypeReference<>() {
            });

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().code()).isEqualTo("SUCC-201");
        SignUpResponse signUpResponse = response.getBody().data();
        assertThat(signUpResponse.email()).isEqualTo(request.email());
        assertThat(signUpResponse.accountId()).isNotNull();
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 중복")
    void signUp_fail_duplicatedEmail() {
        // given
        // 먼저 사용자를 하나 가입시킴
        SignUpRequest initialRequest = new SignUpRequest("test@email.com", "Password123!", "testuser", "안녕하세요", "INFP");
        restTemplate.exchange(
            "/api/v1/auth/signup",
            HttpMethod.POST,
            new HttpEntity<>(initialRequest),
            new ParameterizedTypeReference<GlobalApiResponse<SignUpResponse>>() {
            });

        // when
        // 동일한 이메일로 다시 가입 요청
        SignUpRequest duplicatedRequest = new SignUpRequest("test@email.com", "password456!", "anotheruser", "반갑습니다",
            "ENTJ");
        ResponseEntity<GlobalApiResponse<Map<String, String>>> response = restTemplate.exchange(
            "/api/v1/auth/signup",
            HttpMethod.POST,
            new HttpEntity<>(duplicatedRequest),
            new ParameterizedTypeReference<>() {
            });

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("회원가입 실패 - 닉네임 중복")
    void signUp_fail_duplicatedNickname() {
        // given
        // 먼저 사용자를 하나 가입시킴
        SignUpRequest initialRequest = new SignUpRequest("test@email.com", "Password123!", "testuser", "안녕하세요", "INFP");
        restTemplate.exchange(
            "/api/v1/auth/signup",
            HttpMethod.POST,
            new HttpEntity<>(initialRequest),
            new ParameterizedTypeReference<GlobalApiResponse<SignUpResponse>>() {
            });

        // when
        // 동일한 닉네임으로 다시 가입 요청
        SignUpRequest duplicatedRequest = new SignUpRequest("another@email.com", "password456!", "testuser", "반갑습니다",
            "ENTJ");
        ResponseEntity<GlobalApiResponse<Map<String, String>>> response = restTemplate.exchange(
            "/api/v1/auth/signup",
            HttpMethod.POST,
            new HttpEntity<>(duplicatedRequest),
            new ParameterizedTypeReference<>() {
            });

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @ParameterizedTest
    @MethodSource("invalidSignUpRequests")
    @DisplayName("회원가입 실패 - 잘못된 입력값")
    void signUp_fail_invalidRequest(SignUpRequest request) {
        // when
        ResponseEntity<GlobalApiResponse<Map<String, String>>> response = restTemplate.exchange(
            "/api/v1/auth/signup",
            HttpMethod.POST,
            new HttpEntity<>(request),
            new ParameterizedTypeReference<>() {
            });

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    private static Stream<Arguments> invalidSignUpRequests() {
        return Stream.of(
            // Invalid Email
            Arguments.of(new SignUpRequest("invalid-email", "Password123!", "testuser", "hi", "INFP")),
            // Short Password
            Arguments.of(new SignUpRequest("test@email.com", "pass", "testuser", "hi", "INFP")),
            // Password without number
            Arguments.of(new SignUpRequest("test@email.com", "password!", "testuser", "hi", "INFP")),
            // Password without special character
            Arguments.of(new SignUpRequest("test@email.com", "password123", "testuser", "hi", "INFP")),
            // Password without letter
            Arguments.of(new SignUpRequest("test@email.com", "12345678!", "testuser", "hi", "INFP")),
            // Blank Nickname
            Arguments.of(new SignUpRequest("test@email.com", "Password123!", "", "hi", "INFP")));
    }
}