package org.nfactorial.newsfeed.domain.profile.repository;

import org.nfactorial.newsfeed.domain.profile.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
	boolean existsByNickname(String nickname);

	boolean existsByNicknameAndIdNot(String nickname, Long id);
}
