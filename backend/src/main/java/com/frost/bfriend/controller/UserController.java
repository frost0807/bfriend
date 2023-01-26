package com.frost.bfriend.controller;

import com.frost.bfriend.service.UserService;
import com.frost.bfriend.util.cookie.CookieHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;

import static com.frost.bfriend.constants.CookieConstants.*;
import static com.frost.bfriend.constants.ResponseConstants.CREATED;
import static com.frost.bfriend.constants.ResponseConstants.OK;
import static com.frost.bfriend.dto.UserDto.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    private final CookieHandler cookieHandler;

    @GetMapping("/email/{email}")
    public ResponseEntity<Boolean> isEmailDuplicated(@PathVariable String email) {
        return ResponseEntity.ok(userService.isEmailDuplicated(email));
    }

    @GetMapping("/phone/{phone}")
    public ResponseEntity<Boolean> isPhoneDuplicated(@PathVariable String phone) {
        return ResponseEntity.ok(userService.isPhoneDuplicated(phone));
    }

    @GetMapping("/email/certification/{email}")
    public ResponseEntity<Void> sendCertificationEmail(@PathVariable String email) {
        userService.sendCertificationEmail(email);

        return OK;
    }

    @PostMapping("/email/certification")
    public ResponseEntity<Void> checkEmailCertificationCode(@RequestBody EmailCertificationRequest request) {
        String identifier = userService.checkEmailCertificationCode(request);
        ResponseCookie emailIdentifierCookie = cookieHandler.createEmailIdentifierCookie(identifier);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, emailIdentifierCookie.toString()).build();
    }

    @GetMapping("/phone/certification/{phone}")
    public ResponseEntity<Void> sendCertificationSms(@PathVariable String phone) {
        userService.sendCertificationSms(phone);

        return OK;
    }

    @PostMapping("/phone/certification")
    public ResponseEntity<Void> checkSmsCertificationCode(@RequestBody SmsCertificationRequest request) {
        String identifier = userService.checkSmsCertificationCode(request);
        ResponseCookie smsIdentifierCookie = cookieHandler.createSmsIdentifierCookie(identifier);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, smsIdentifierCookie.toString()).build();
    }

    @PostMapping
    public ResponseEntity<Void> createUser(
            @RequestBody SaveRequest request,
            @CookieValue(value = EMAIL_CERTIFICATION_IDENTIFIER) Cookie emailIdentifierCookie,
            @CookieValue(value = SMS_CERTIFICATION_IDENTIFIER) Cookie smsIdentifierCookie) {
        userService.createUser(request, emailIdentifierCookie.getValue(), smsIdentifierCookie.getValue());
        cookieHandler.deleteCookie(EMAIL_CERTIFICATION_IDENTIFIER);
        cookieHandler.deleteCookie(SMS_CERTIFICATION_IDENTIFIER);

        return CREATED;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest request) {
        String accessToken = userService.login(request);
        ResponseCookie accessTokenCookie = cookieHandler.createAccessTokenCookie(accessToken);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString()).build();
    }
}