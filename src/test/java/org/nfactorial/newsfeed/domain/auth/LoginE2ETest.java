package org.nfactorial.newsfeed.domain.auth;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.nfactorial.newsfeed.common.dto.GlobalApiResponse;
import org.nfactorial.newsfeed.domain.auth.dto.LoginRequest;
import org.nfactorial.newsfeed.domain.auth.dto.LoginResponse;
import org.nfactorial.newsfeed.domain.auth.dto.SignUpRequest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class LoginE2ETest extends AuthE2ETest {

    @BeforeEach
    void setUp() {
        // 로그인을 테스트하기 위해 미리 회원가입을 시켜놓음
        SignUpRequest request = new SignUpRequest(
            "test@email.com",
            "password123!",
            "testuser",
            "안녕하세요",
            "INFP");
        restTemplate.postForEntity("/api/v1/auth/signup", request, String.class);
    }

    @Test
    @DisplayName("로그인 성공")
    void login_success() {
        // given
        LoginRequest request = new LoginRequest("test@email.com", "password123!");

        // when
        ResponseEntity<GlobalApiResponse<LoginResponse>> response = restTemplate.exchange(
            "/api/v1/auth/login",
            HttpMethod.POST,
            new HttpEntity<>(request),
            new ParameterizedTypeReference<>() {
            });

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().data().accessToken()).isNotBlank();
    }

    @Test
    @DisplayName("로그인 실패 - 존재하지 않는 이메일")
    void login_fail_emailNotFound() {
        // given
        LoginRequest request = new LoginRequest("nonexistent@email.com", "password123!");

        // when
        ResponseEntity<GlobalApiResponse<Void>> response = restTemplate.exchange(
            "/api/v1/auth/login",
            HttpMethod.POST,
            new HttpEntity<>(request),
            new ParameterizedTypeReference<>() {
            });

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 불일치")
    void login_fail_passwordDoesNotMatch() {
        // given
        LoginRequest request = new LoginRequest("test@email.com", "wrongpassword!");

        // when
        ResponseEntity<GlobalApiResponse<Void>> response = restTemplate.exchange(
            "/api/v1/auth/login",
            HttpMethod.POST,
            new HttpEntity<>(request),
            new ParameterizedTypeReference<>() {
            });

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}