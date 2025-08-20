package org.nfactorial.newsfeed.domain.auth.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.nfactorial.newsfeed.domain.auth.dto.LoginRequest;
import org.nfactorial.newsfeed.domain.auth.dto.SignUpCommand;
import org.nfactorial.newsfeed.domain.auth.dto.SignUpRequest;
import org.nfactorial.newsfeed.domain.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
@ExtendWith(MockitoExtension.class)
class AuthControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private AuthService authService;

	@Nested
	@DisplayName("회원가입")
	class SignUp {
		@Test
		@DisplayName("성공")
		void signUp_success() throws Exception {
			// given
			SignUpRequest request = SignUpRequest.builder()
				.email("test@example.com")
				.password("password123!")
				.nickname("testuser")
				.build();

			// when & then
			mockMvc.perform(post("/api/v1/auth/signup")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.code").value("SUCC-201"))
				.andExpect(jsonPath("$.data.email").value("test@example.com"))
				.andExpect(jsonPath("$.data.nickname").value("testuser"))
				.andDo(print());
		}

		@Test
		@DisplayName("실패 - 중복된 이메일")
		void signUp_fail_duplicateEmail() throws Exception {
			// given
			// 먼저 사용자를 하나 가입시킨다.
			SignUpRequest initialRequest = SignUpRequest.builder()
				.email("test@example.com")
				.password("password123!")
				.nickname("testuser1")
				.build();
			mockMvc.perform(post("/api/v1/auth/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(initialRequest)));

			// 동일한 이메일로 다시 가입 시도
			SignUpRequest duplicateRequest = SignUpRequest.builder()
				.email("test@example.com")
				.password("password123!")
				.nickname("testuser2")
				.build();

			// when & then
			mockMvc.perform(post("/api/v1/auth/signup")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(duplicateRequest)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code").value("AUTH-400"))
				.andDo(print());
		}

		@Test
		@DisplayName("실패 - 중복된 닉네임")
		void signUp_fail_duplicateNickname() throws Exception {
			// given
			SignUpRequest request = SignUpRequest.builder()
				.email("test@example.com")
				.password("password123!")
				.nickname("duplicated-nickname")
				.build();

			SignUpCommand command = SignUpCommand.of(request);
			authService.signUp(command);

			// when & then
			mockMvc.perform(post("/api/v1/auth/signup")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code").value("AUTH-400"))
				.andDo(print());
		}

		@Test
		@DisplayName("실패 - 유효하지 않은 비밀번호 (8자 미만)")
		void signUp_fail_invalidPassword_tooShort() throws Exception {
			// given
			SignUpRequest request = SignUpRequest.builder()
				.email("test@example.com")
				.password("pass1!")
				.nickname("testuser")
				.build();

			// when & then
			mockMvc.perform(post("/api/v1/auth/signup")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest())
				.andDo(print());
		}

		@Test
		@DisplayName("실패 - 유효하지 않은 비밀번호 (패턴 불일치)")
		void signUp_fail_invalidPassword_patternMismatch() throws Exception {
			// given
			SignUpRequest request = SignUpRequest.builder()
				.email("test@example.com")
				.password("password123") // 특수문자 없음
				.nickname("testuser")
				.build();

			// when & then
			mockMvc.perform(post("/api/v1/auth/signup")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest())
				.andDo(print());
		}

		@Test
		@DisplayName("실패 - 빈 이메일")
		void signUp_fail_blankEmail() throws Exception {
			// given
			SignUpRequest request = SignUpRequest.builder()
				.email("")
				.password("password123!")
				.nickname("testuser")
				.build();

			// when & then
			mockMvc.perform(post("/api/v1/auth/signup")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest())
				.andDo(print());
		}
	}

	@Nested
	@DisplayName("로그인")
	class Login {
		@BeforeEach
		void setUp() {
			SignUpRequest signUpRequest = new SignUpRequest("test@test.com", "password", "testuser", null, null);
			authService.signUp(SignUpCommand.of(signUpRequest));
		}

		@Test
		@DisplayName("로그인 성공")
		void loginSuccess() throws Exception {
			// given
			LoginRequest loginRequest = new LoginRequest("test@test.com", "password");

			// when & then
			mockMvc.perform(post("/api/v1/auth/login")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(loginRequest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.accessToken").exists());
		}

		@Test
		@DisplayName("로그인 실패 - 잘못된 비밀번호")
		void loginFailWrongPassword() throws Exception {
			// given
			LoginRequest loginRequest = new LoginRequest("test@test.com", "wrongpassword");

			// when & then
			mockMvc.perform(post("/api/v1/auth/login")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(loginRequest)))
				.andExpect(status().isUnauthorized());
		}

		@Test
		@DisplayName("로그인 실패 - 존재하지 않는 이메일")
		void loginFailWrongEmail() throws Exception {
			// given
			LoginRequest loginRequest = new LoginRequest("wrong@test.com", "password");

			// when & then
			mockMvc.perform(post("/api/v1/auth/login")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(loginRequest)))
				.andExpect(status().isUnauthorized());
		}
	}
}

