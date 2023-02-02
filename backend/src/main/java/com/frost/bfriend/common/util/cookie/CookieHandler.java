package com.frost.bfriend.common.util.cookie;

import com.frost.bfriend.common.constants.EmailConstants;
import com.frost.bfriend.common.constants.JwtConstants;
import com.frost.bfriend.common.constants.SmsConstants;
import com.frost.bfriend.exception.user.CookieNotFoundException;
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
                .path("/")
                .maxAge(EmailConstants.EMAIL_CERTIFICATION_IDENTIFIER_EXPIRY_SECONDS)
                .build();
    }

    public ResponseCookie createSmsIdentifierCookie(String identifier) {
        return ResponseCookie.from(SMS_CERTIFICATION_IDENTIFIER, identifier)
                .httpOnly(true)
                .path("/")
                .maxAge(SmsConstants.SMS_CERTIFICATION_IDENTIFIER_EXPIRY_SECONDS)
                .build();
    }

    public ResponseCookie createAccessTokenCookie(String accessToken) {
        return ResponseCookie.from(ACCESS_TOKEN, accessToken)
                .httpOnly(true)
                .path("/")
                .maxAge(JwtConstants.ACCESS_TOKEN_EXPIRY_SECONDS)
                .build();
    }

    public Cookie parseAccessTokenCookie(HttpServletRequest request) {
        isCookieExist(request);
        return Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals(ACCESS_TOKEN))
                .findFirst()
                .orElseThrow(() -> new NotLoggedInException("로그인이 만료되었습니다. 다시 로그인 해주세요"));
    }

    private void isCookieExist(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new CookieNotFoundException("로그인이 필요합니다.");
        }
    }

    public ResponseCookie expireCookie(String cookieName) {
        return ResponseCookie.from(cookieName, null)
                .maxAge(0)
                .path("/")
                .build();
    }
}
