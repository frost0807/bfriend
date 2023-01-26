package com.frost.bfriend.util.cookie;

import com.frost.bfriend.constants.EmailConstants;
import com.frost.bfriend.constants.JwtConstants;
import com.frost.bfriend.constants.SmsConstants;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import static com.frost.bfriend.constants.CookieConstants.*;

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

    public ResponseCookie deleteCookie(String cookieName) {
        return ResponseCookie.from(cookieName, null)
                .maxAge(0)
                .build();
    }
}
