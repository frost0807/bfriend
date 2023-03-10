package com.frost.bfriend.common.util.interceptor;

import com.frost.bfriend.common.annotation.CheckUser;
import com.frost.bfriend.common.util.cookie.CookieHandler;
import com.frost.bfriend.common.util.jwt.TokenProvider;
import com.frost.bfriend.dto.UserDto;
import com.frost.bfriend.dto.UserDto.UserIdAndLevel;
import com.frost.bfriend.exception.user.NotLoggedInException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Check;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.frost.bfriend.common.constants.JwtConstants.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginCheckInterceptor implements HandlerInterceptor {

    private final CookieHandler cookieHandler;

    private final TokenProvider tokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info("JwtAuthenticationInterceptor preHandle");
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            CheckUser checkUser = handlerMethod.getMethodAnnotation(CheckUser.class);
            if (checkUser == null) {
                return true;
            }
        }
        String accessToken = cookieHandler.parseAccessTokenCookie(request).getValue();
        if (accessToken == null || accessToken.equalsIgnoreCase("null")) {
            throw new NotLoggedInException("토큰이 존재하지 않습니다.");
        }
        UserIdAndLevel userIdAndLevel = tokenProvider.validateAndGetUserIdAndLevel(accessToken);
        request.setAttribute(USERID_ATTRIBUTE, userIdAndLevel.getUserId());
        request.setAttribute(USER_LEVEL, userIdAndLevel.getUserLevel());

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
