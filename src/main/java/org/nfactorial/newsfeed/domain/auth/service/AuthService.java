package org.nfactorial.newsfeed.domain.auth.service;

import java.util.Optional;

import org.nfactorial.newsfeed.common.code.ErrorCode;
import org.nfactorial.newsfeed.common.exception.BusinessException;
import org.nfactorial.newsfeed.common.security.AuthProfileDto;
import org.nfactorial.newsfeed.common.security.JwtUtil;
import org.nfactorial.newsfeed.common.security.TokenBlacklist;
import org.nfactorial.newsfeed.domain.auth.component.PasswordEncoder;
import org.nfactorial.newsfeed.domain.auth.dto.ChangePasswordCommand;
import org.nfactorial.newsfeed.domain.auth.dto.LoginCommand;
import org.nfactorial.newsfeed.domain.auth.dto.SignUpCommand;
import org.nfactorial.newsfeed.domain.auth.dto.SignUpResult;
import org.nfactorial.newsfeed.domain.auth.entity.Account;
import org.nfactorial.newsfeed.domain.auth.repository.AccountRepository;
import org.nfactorial.newsfeed.domain.profile.dto.request.CreateProfileCommand;
import org.nfactorial.newsfeed.domain.profile.service.ProfileServiceApi;
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
	private final ProfileServiceApi profileService;
	private final JwtUtil jwtUtil;
	private final TokenBlacklist tokenBlacklist;

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
		CreateProfileCommand createProfileCommand = CreateProfileCommand.builder()
			.nickname(signUpCommand.nickname())
			.mbti(signUpCommand.mbti())
			.introduce(signUpCommand.introduce())
			.build();
		long profiledId = profileService.createProfile(createProfileCommand);
		Account newAccount = Account.signUp(signUpCommand.email(), encodedPassword, profiledId);
		Account savedAccount = accountRepository.save(newAccount);

		return SignUpResult.builder()
			.accountId(savedAccount.getId())
			.email(savedAccount.getEmail())
			.build();
	}

	@Transactional(readOnly = true)
	public String login(LoginCommand loginCommand) {
		Account account = accountRepository.findByEmail(loginCommand.email())
			.orElseThrow(() -> new BusinessException(ErrorCode.LOGIN_FAILED));
		if (passwordencoder.matches(loginCommand.password(), account.getPassword()) == false) {
			throw new BusinessException(ErrorCode.LOGIN_FAILED);
		}
		return jwtUtil.createToken(account.getId());
	}

	@Transactional(readOnly = true)
	public Account getAccountById(long accountId) {
		return accountRepository.findById(accountId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND));
	}

	@Transactional
	public void withdraw(AuthProfileDto authProfile, String password) {
		long accountId = authProfile.accountId();
		Account account = accountRepository.findById(accountId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND));
		if (passwordencoder.matches(password, account.getPassword()) == false) {
			throw new BusinessException(ErrorCode.PASSWORD_DOESNT_MATCH);
		}
		account.setDeleted();
		profileService.deleteFromAccountId(accountId);
	}

	public void logout(Optional<String> token) {
		String tokenStr = token.orElseThrow(() -> new BusinessException(ErrorCode.INVALID_TOKEN));
		tokenBlacklist.addToken(tokenStr);
	}

	@Transactional
	public void changePassword(ChangePasswordCommand command) {
		Account account = accountRepository.findById(command.accountId())
			.orElseThrow(() -> new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND));

		if (passwordencoder.matches(command.currentPassword(), account.getPassword()) == false) {
			throw new BusinessException(ErrorCode.PASSWORD_DOESNT_MATCH);
		}

		String encodedPassword = passwordencoder.encode(command.changePassword());
		account.changePassword(encodedPassword);
	}
}
