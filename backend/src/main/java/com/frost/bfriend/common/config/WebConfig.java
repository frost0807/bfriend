package com.frost.bfriend.common.config;

import com.frost.bfriend.common.util.interceptor.LoginCheckInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final LoginCheckInterceptor loginCheckInterceptor;

    private final LoginUserArgumentResolver loginUserArgumentResolver;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        log.info("cors");
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8081")
                .allowCredentials(true)
                .allowedHeaders("*")
                .allowedMethods("*");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("inter123");
        registry.addInterceptor(loginCheckInterceptor)
                .excludePathPatterns("/*", "/**")
                .order(1);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginUserArgumentResolver);
    }
}