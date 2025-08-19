package org.nfactorial.newsfeed.domain.auth.repository;

import java.util.Optional;

import org.nfactorial.newsfeed.domain.auth.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
	boolean existsByEmail(String email);

	Optional<Account> findByEmail(String email);
}
