package org.nfactorial.newsfeed.domain.auth;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.nfactorial.newsfeed.common.dto.GlobalApiResponse;
import org.nfactorial.newsfeed.domain.auth.dto.ChangePasswordRequest;
import org.nfactorial.newsfeed.domain.auth.dto.LoginRequest;
import org.nfactorial.newsfeed.domain.auth.dto.LoginResponse;
import org.nfactorial.newsfeed.domain.auth.dto.SignUpRequest;
import org.nfactorial.newsfeed.domain.auth.dto.WithdrawRequest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class AuthRequiredE2ETest extends AuthE2ETest {
    private String jwtToken;

    @BeforeEach
    void setUp() {
        // 사용자 가입
        SignUpRequest signUpRequest = new SignUpRequest("test@email.com", "Password123!", "testuser", "안녕하세요", "INFP");
        restTemplate.postForEntity("/api/v1/auth/signup", signUpRequest, String.class);

        // 로그인 후 토큰 획득
        LoginRequest loginRequest = new LoginRequest("test@email.com", "Password123!");
        ResponseEntity<GlobalApiResponse<LoginResponse>> response = restTemplate.exchange(
            "/api/v1/auth/login",
            HttpMethod.POST,
            new HttpEntity<>(loginRequest),
            new ParameterizedTypeReference<>() {
            });
        this.jwtToken = response.getBody().data().accessToken();
    }

    @Test
    @DisplayName("비밀번호 변경 성공")
    void changePassword_success() {
        // given
        ChangePasswordRequest request = new ChangePasswordRequest("Password123!", "newPassword456!");
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<ChangePasswordRequest> entity = new HttpEntity<>(request, headers);

        // when
        ResponseEntity<GlobalApiResponse<Void>> response = restTemplate.exchange(
            "/api/v1/auth/me/password",
            HttpMethod.PATCH,
            entity,
            new ParameterizedTypeReference<>() {
            });

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("비밀번호 변경 실패 - 현재 비밀번호 불일치")
    void changePassword_fail_passwordDoesNotMatch() {
        // given
        ChangePasswordRequest request = new ChangePasswordRequest("wrongPassword!", "newPassword456!");
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<ChangePasswordRequest> entity = new HttpEntity<>(request, headers);

        // when
        ResponseEntity<GlobalApiResponse<Void>> response = restTemplate.exchange(
            "/api/v1/auth/me/password",
            HttpMethod.PATCH,
            entity,
            new ParameterizedTypeReference<>() {
            });

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("비밀번호 변경 실패 - 현재 사용중인 비밀번호")
    void changePassword_fail_currentPasswordInUse() {
        // given
        ChangePasswordRequest request = new ChangePasswordRequest("Password123!", "Password123!");
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<ChangePasswordRequest> entity = new HttpEntity<>(request, headers);

        // when
        ResponseEntity<GlobalApiResponse<Void>> response = restTemplate.exchange(
            "/api/v1/auth/me/password",
            HttpMethod.PATCH,
            entity,
            new ParameterizedTypeReference<>() {
            });

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("로그아웃 성공")
    void logout_success() {
        // given
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // when
        ResponseEntity<GlobalApiResponse<Void>> response = restTemplate.exchange(
            "/api/v1/auth/logout",
            HttpMethod.POST,
            entity,
            new ParameterizedTypeReference<>() {
            });

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // and when - 로그아웃한 토큰으로 다시 요청
        ChangePasswordRequest request = new ChangePasswordRequest("Password123!", "newPassword456!");
        HttpEntity<ChangePasswordRequest> nextEntity = new HttpEntity<>(request, headers);
        ResponseEntity<GlobalApiResponse<Void>> nextResponse = restTemplate.exchange(
            "/api/v1/auth/me/password",
            HttpMethod.PATCH,
            nextEntity,
            new ParameterizedTypeReference<>() {
            });

        // then - 실패해야 함
        assertThat(nextResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("회원탈퇴 성공")
    void withdraw_success() {
        // given
        WithdrawRequest request = new WithdrawRequest("Password123!");
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<WithdrawRequest> entity = new HttpEntity<>(request, headers);

        // when
        ResponseEntity<GlobalApiResponse<Void>> response = restTemplate.exchange(
            "/api/v1/auth/withdraw",
            HttpMethod.POST,
            entity,
            new ParameterizedTypeReference<>() {
            });

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("회원탈퇴 실패 - 비밀번호 불일치")
    void withdraw_fail_passwordDoesNotMatch() {
        // given
        WithdrawRequest request = new WithdrawRequest("wrongPassword!");
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<WithdrawRequest> entity = new HttpEntity<>(request, headers);

        // when
        ResponseEntity<GlobalApiResponse<Void>> response = restTemplate.exchange(
            "/api/v1/auth/withdraw",
            HttpMethod.POST,
            entity,
            new ParameterizedTypeReference<>() {
            });

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}