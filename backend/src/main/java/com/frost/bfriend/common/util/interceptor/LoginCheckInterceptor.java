package com.frost.bfriend.common.util.interceptor;

import com.frost.bfriend.common.constants.JwtConstants;
import com.frost.bfriend.common.util.cookie.CookieHandler;
import com.frost.bfriend.common.util.jwt.TokenProvider;
import com.frost.bfriend.exception.user.NotLoggedInException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginCheckInterceptor implements HandlerInterceptor {

    private final CookieHandler cookieHandler;

    private final TokenProvider tokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info("JwtAuthenticationInterceptor preHandle");
        String accessToken = cookieHandler.parseAccessTokenCookie(request).getValue();

        if(accessToken == null || accessToken.equalsIgnoreCase("null")) {
            throw new NotLoggedInException("토큰이 존재하지 않습니다.");
        }
        long userId = tokenProvider.validateAndGetUserId(accessToken);
        request.setAttribute(JwtConstants.USERID_ATTRIBUTE, userId);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("JwtAuthenticationInterceptor postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("JwtAuthenticationInterceptor afterCompletion");
    }
}
