package org.nfactorial.newsfeed.domain.profile.controller;

import org.nfactorial.newsfeed.common.code.SuccessCode;
import org.nfactorial.newsfeed.common.dto.GlobalApiResponse;
import org.nfactorial.newsfeed.common.security.AuthProfile;
import org.nfactorial.newsfeed.common.security.AuthProfileDto;
import org.nfactorial.newsfeed.domain.profile.dto.request.UpdateProfileCommand;
import org.nfactorial.newsfeed.domain.profile.dto.response.ProfileResponse;
import org.nfactorial.newsfeed.domain.profile.service.ProfileServiceApi;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProfileController {

	private final ProfileServiceApi profileServiceApi;


	@GetMapping("/api/v1/profiles/{id}")
	public GlobalApiResponse<ProfileResponse> getProfile(@PathVariable long profileId) {
		ProfileResponse response = profileServiceApi.getProfileById(profileId);
		return GlobalApiResponse.of(SuccessCode.OK, response);
	}

	@PutMapping("/api/v1/profiles/me")
	public GlobalApiResponse<ProfileResponse> updateMyProfile(
		@RequestBody UpdateProfileCommand command,
		@AuthProfile AuthProfileDto currentProfile)
	{
		ProfileResponse response = profileServiceApi.updateProfile(currentProfile.profileId(), command);
		return GlobalApiResponse.of(SuccessCode.OK, response);
	}
}
