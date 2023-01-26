package com.frost.bfriend.util.jwt;

import com.frost.bfriend.exception.user.NotLoggedInException;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Arrays;

import static com.frost.bfriend.constants.JwtConstants.USERID_ATTRIBUTE;

@Slf4j
//@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        log.info("JwtAuthenticationFilter");
        try {
            String accessToken = parseAccessToken(request);

            if(accessToken == null || accessToken.equalsIgnoreCase("null")) {
                throw new NotLoggedInException("토큰이 존재하지 않습니다.");
            }

            long userId = tokenProvider.validateAndGetUserId(accessToken);
            request.setAttribute(USERID_ATTRIBUTE, userId);
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException exception) {
            log.error("토큰이 만료되었습니다.");
        }

    }

    private String parseAccessToken(HttpServletRequest httpServletRequest) {
        String token = Arrays.stream(httpServletRequest.getCookies())
                .filter(c -> c.getName().equals("bfriend-access-token"))
                .findFirst()
                .orElseThrow(() -> new NotLoggedInException("로그인이 되지 않았습니다."))
                .getValue();
        return token;
    }
}