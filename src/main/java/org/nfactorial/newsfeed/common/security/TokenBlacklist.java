package org.nfactorial.newsfeed.common.security;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class TokenBlacklist {
	private static final Set<String> BLACK_LIST = ConcurrentHashMap.newKeySet();

	public void addToken(String token) {
		BLACK_LIST.add(token);
	}

	public boolean hasToken(String token) {
		return BLACK_LIST.contains(token);
	}
}
