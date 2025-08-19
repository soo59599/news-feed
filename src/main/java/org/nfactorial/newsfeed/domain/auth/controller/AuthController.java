package org.nfactorial.newsfeed.domain.auth.controller;

import org.nfactorial.newsfeed.common.code.SuccessCode;
import org.nfactorial.newsfeed.common.dto.GlobalApiResponse;
import org.nfactorial.newsfeed.domain.auth.dto.LoginCommand;
import org.nfactorial.newsfeed.domain.auth.dto.LoginRequest;
import org.nfactorial.newsfeed.domain.auth.dto.LoginResponse;
import org.nfactorial.newsfeed.domain.auth.dto.SignUpCommand;
import org.nfactorial.newsfeed.domain.auth.dto.SignUpRequest;
import org.nfactorial.newsfeed.domain.auth.dto.SignUpResponse;
import org.nfactorial.newsfeed.domain.auth.dto.SignUpResult;
import org.nfactorial.newsfeed.domain.auth.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
	private final AuthService authService;

	@PostMapping("/signup")
	@ResponseStatus(HttpStatus.CREATED)
	public GlobalApiResponse<?> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
		SignUpCommand signUpCommand = SignUpCommand.of(signUpRequest);
		SignUpResult result = authService.signUp(signUpCommand);
		SignUpResponse response = SignUpResponse.of(result);
		return GlobalApiResponse.of(SuccessCode.CREATED, response);
	}

	@PostMapping("/login")
	public GlobalApiResponse<?> login(@Valid @RequestBody LoginRequest loginRequest) {
		LoginCommand loginCommand = LoginCommand.of(loginRequest);
		String token = authService.login(loginCommand);
		return GlobalApiResponse.of(SuccessCode.OK, new LoginResponse(token));
	}
}
