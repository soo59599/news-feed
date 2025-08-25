package org.nfactorial.newsfeed.domain.auth;

import static org.assertj.core.api.Assertions.*;

import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.nfactorial.newsfeed.common.dto.GlobalApiResponse;
import org.nfactorial.newsfeed.domain.auth.dto.LoginRequest;
import org.nfactorial.newsfeed.domain.auth.dto.LoginResponse;
import org.nfactorial.newsfeed.domain.auth.dto.SignUpRequest;
import org.nfactorial.newsfeed.domain.auth.dto.WithdrawRequest;
import org.springframework.http.HttpHeaders;
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
            "Password123!",
            "testuser",
            "안녕하세요",
            "INFP");
        restTemplate.postForEntity("/api/v1/auth/signup", request, String.class);
    }

    @Test
    @DisplayName("로그인 성공")
    void login_success() {
        // given
        LoginRequest request = new LoginRequest("test@email.com", "Password123!");

        // when
        ResponseEntity<GlobalApiResponse<LoginResponse>> response = restTemplate.exchange(
            "/api/v1/auth/login",
            HttpMethod.POST,
            new HttpEntity<>(request),
            new ParameterizedTypeReference<>() {
            });

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        GlobalApiResponse<LoginResponse> body = Objects.requireNonNull(response.getBody());
        assertThat(body.data().accessToken()).isNotBlank();
    }

    @Test
    @DisplayName("로그인 실패 - 존재하지 않는 이메일")
    void login_fail_emailNotFound() {
        // given
        LoginRequest request = new LoginRequest("nonexistent@email.com", "Password123!");

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

    @Test
    @DisplayName("로그인 실패 - 탈퇴한 회원")
    void login_fail_withdrawnAccount() {
        // given: 이미 setUp()에서 회원가입이 되어 있으므로 JWT를 얻어 회원탈퇴를 수행
        LoginRequest loginReq = new LoginRequest("test@email.com", "Password123!");

        // 로그인해서 토큰 획득
        ResponseEntity<GlobalApiResponse<LoginResponse>> loginResponse = restTemplate.exchange(
            "/api/v1/auth/login",
            HttpMethod.POST,
            new HttpEntity<>(loginReq),
            new ParameterizedTypeReference<>() {
            });

        GlobalApiResponse<LoginResponse> loginBody = java.util.Objects.requireNonNull(loginResponse.getBody());
        String token = loginBody.data().accessToken();

        // 회원탈퇴 요청
        WithdrawRequest withdrawRequest = new WithdrawRequest("Password123!");
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<WithdrawRequest> withdrawEntity = new HttpEntity<>(withdrawRequest, headers);

        ResponseEntity<GlobalApiResponse<Void>> withdrawResponse = restTemplate.exchange(
            "/api/v1/auth/withdraw",
            HttpMethod.POST,
            withdrawEntity,
            new ParameterizedTypeReference<>() {
            });
        assertThat(withdrawResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        // when: 탈퇴한 계정으로 로그인 시도
        ResponseEntity<GlobalApiResponse<Void>> afterResponse = restTemplate.exchange(
            "/api/v1/auth/login",
            HttpMethod.POST,
            new HttpEntity<>(loginReq),
            new ParameterizedTypeReference<>() {
            });

        // then
        assertThat(afterResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}