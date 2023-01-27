package com.frost.bfriend.common.util.cookie;

import com.frost.bfriend.common.constants.EmailConstants;
import com.frost.bfriend.common.constants.JwtConstants;
import com.frost.bfriend.common.constants.SmsConstants;
import com.frost.bfriend.exception.user.NotLoggedInException;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import java.util.Arrays;

import static com.frost.bfriend.common.constants.CookieConstants.*;

@Component
public class CookieHandler {

    public ResponseCookie createEmailIdentifierCookie(String identifier) {
        return ResponseCookie.from(EMAIL_CERTIFICATION_IDENTIFIER, identifier)
                .httpOnly(true)
                .secure(true)
                .path("/users/certification")
                .maxAge(EmailConstants.EMAIL_CERTIFICATION_IDENTIFIER_DURATION)
                .build();
    }

    public ResponseCookie createSmsIdentifierCookie(String identifier) {
        return ResponseCookie.from(SMS_CERTIFICATION_IDENTIFIER, identifier)
                .httpOnly(true)
                .secure(true)
                .path("/users/certification")
                .maxAge(SmsConstants.SMS_CERTIFICATION_IDENTIFIER_DURATION)
                .build();
    }

    public ResponseCookie createAccessTokenCookie(String accessToken) {
        return ResponseCookie.from(ACCESS_TOKEN, accessToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(JwtConstants.ACCESS_TOKEN_EXPIRY_MINUTES)
                .build();
    }

    public Cookie parseAccessTokenCookie(HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals("bfriend-access-token"))
                .findFirst()
                .orElseThrow(() -> new NotLoggedInException("로그인이 되지 않았습니다."));
    }

    public ResponseCookie expireCookie(String cookieName) {
        return ResponseCookie.from(cookieName, null)
                .maxAge(0)
                .build();
    }
}
