package org.nfactorial.newsfeed.domain.auth.component;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoder {
	public String encode(String password) {
		return BCrypt.hashpw(password, BCrypt.gensalt());
	}

	public boolean matches(String password, String encodedPassword) {
		return BCrypt.checkpw(password, encodedPassword);
	}
}
