package org.nfactorial.newsfeed.domain.auth.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
public class AuthControllerLoginTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private AuthService authService;

	@Autowired
	private ObjectMapper objectMapper;

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
