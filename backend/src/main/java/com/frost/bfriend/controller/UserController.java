package com.frost.bfriend.controller;

import com.frost.bfriend.common.util.cookie.CookieHandler;
import com.frost.bfriend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;

import static com.frost.bfriend.common.constants.CookieConstants.EMAIL_CERTIFICATION_IDENTIFIER;
import static com.frost.bfriend.common.constants.CookieConstants.SMS_CERTIFICATION_IDENTIFIER;
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

        return ResponseEntity.ok().build();
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

        return ResponseEntity.ok().build();
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
        ResponseCookie deletedEmailCookie = cookieHandler.expireCookie(EMAIL_CERTIFICATION_IDENTIFIER);
        ResponseCookie deletedSmsCookie = cookieHandler.expireCookie(SMS_CERTIFICATION_IDENTIFIER);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,
                deletedEmailCookie.toString(), deletedSmsCookie.toString()).build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        TokenAndName tokenAndName = userService.login(request);
        ResponseCookie accessTokenCookie = cookieHandler.createAccessTokenCookie(tokenAndName.getToken());
        String name = tokenAndName.getName();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString()).body(name);
    }

}