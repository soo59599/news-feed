package org.nfactorial.newsfeed.common.config;

import java.util.List;

import org.nfactorial.newsfeed.common.security.AuthProfileMethodArgResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthProfileMethodArgResolver authProfileMethodArgResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authProfileMethodArgResolver);
    }
}
