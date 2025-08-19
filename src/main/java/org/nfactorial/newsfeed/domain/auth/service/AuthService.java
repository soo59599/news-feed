package org.nfactorial.newsfeed.domain.auth.service;

import org.nfactorial.newsfeed.common.code.ErrorCode;
import org.nfactorial.newsfeed.common.exception.BusinessException;
import org.nfactorial.newsfeed.common.security.JwtUtil;
import org.nfactorial.newsfeed.domain.auth.component.PasswordEncoder;
import org.nfactorial.newsfeed.domain.auth.dto.LoginCommand;
import org.nfactorial.newsfeed.domain.auth.dto.SignUpCommand;
import org.nfactorial.newsfeed.domain.auth.dto.SignUpResult;
import org.nfactorial.newsfeed.domain.auth.entity.Account;
import org.nfactorial.newsfeed.domain.auth.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
	private final AccountRepository accountRepository;
	private final PasswordEncoder passwordencoder;
	private final AuthProfileServiceApi profileService;
	private final JwtUtil jwtUtil;

	@Transactional
	public SignUpResult signUp(SignUpCommand signUpCommand) {
		// 이메일 중복 여부 검사
		boolean isEmailDuplicated = accountRepository.existsByEmail(signUpCommand.email());
		if (isEmailDuplicated) {
			log.error("Duplicated email: {}", signUpCommand.email());
			throw new BusinessException(ErrorCode.EMAIL_DUPLICATED);
		}

		// 닉네임 중복 여부 검사
		boolean isNicknameDuplicated = profileService.isNicknameDuplicated(signUpCommand.nickname());
		if (isNicknameDuplicated) {
			log.error("Duplicated nickname: {}", signUpCommand.nickname());
			throw new BusinessException(ErrorCode.NICKNAME_DUPLICATED);
		}

		String encodedPassword = passwordencoder.encode(signUpCommand.password());
		Account newAccount = Account.signUp(signUpCommand.email(), encodedPassword);
		Account savedAccount = accountRepository.save(newAccount);
		AuthProfileServiceApi.CreateProfileCommand createProfileCommand = AuthProfileServiceApi.CreateProfileCommand.builder()
			.accountId(savedAccount.getId())
			.nickname(signUpCommand.nickname())
			.mbti(signUpCommand.mbti())
			.introduce(signUpCommand.introduce())
			.build();
		String savedNickname = profileService.createProfile(createProfileCommand);

		return SignUpResult.builder()
			.accountId(savedAccount.getId())
			.email(savedAccount.getEmail())
			.nickname(savedNickname)
			.build();
	}

	public String login(LoginCommand loginCommand) {
		Account account = accountRepository.findByEmail(loginCommand.email())
			.orElseThrow(() -> new BusinessException(ErrorCode.LOGIN_FAILED));
		if (passwordencoder.matches(loginCommand.password(), account.getPassword()) == false) {
			throw new BusinessException(ErrorCode.LOGIN_FAILED);
		}
		return jwtUtil.createToken(account.getId());
	}
}
