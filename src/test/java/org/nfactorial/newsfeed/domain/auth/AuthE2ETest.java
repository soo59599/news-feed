package org.nfactorial.newsfeed.domain.auth;

import org.junit.jupiter.api.AfterEach;
import org.nfactorial.newsfeed.domain.auth.repository.AccountRepository;
import org.nfactorial.newsfeed.domain.profile.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AuthE2ETest {

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    protected AccountRepository accountRepository;

    @Autowired
    protected ProfileRepository profileRepository;

    @AfterEach
    void tearDown() {
        // 테스트 순서 보장을 위해 repository clear
        accountRepository.deleteAll();
        profileRepository.deleteAll();
    }
}