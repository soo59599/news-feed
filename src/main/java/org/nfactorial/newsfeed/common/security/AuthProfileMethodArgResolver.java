package org.nfactorial.newsfeed.common.security;

import org.nfactorial.newsfeed.common.code.ErrorCode;
import org.nfactorial.newsfeed.common.exception.BusinessException;
import org.nfactorial.newsfeed.common.security.mock.SecurityMockProfile;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthProfileMethodArgResolver implements HandlerMethodArgumentResolver {
	private final GetProfileFromAccountIdApi getProfileFromAccountIdApi;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(AuthProfile.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		final var request = (HttpServletRequest)webRequest.getNativeRequest();
		long accountId = getAccountId(request);
		SecurityMockProfile profile = getProfileFromAccountIdApi.execute(accountId);
		return profile;
	}

	private long getAccountId(HttpServletRequest request) {
		final Object attribute = request.getAttribute(JwtFilter.ACCOUNT_ID_ATTR);
		if (attribute == null) {
			throw new BusinessException(ErrorCode.NOT_AUTHENTICATED);
		}
		try {
			return Long.parseLong(attribute.toString());
		} catch (NumberFormatException e) {
			throw new BusinessException(ErrorCode.NOT_AUTHENTICATED);
		}
	}
}
