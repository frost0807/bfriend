package com.frost.bfriend.controller;

import com.frost.bfriend.common.annotation.CheckUser;
import com.frost.bfriend.common.annotation.LoginUser;
import com.frost.bfriend.common.constants.CookieConstants;
import com.frost.bfriend.common.util.cookie.CookieHandler;
import com.frost.bfriend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

import static com.frost.bfriend.common.constants.CookieConstants.EMAIL_CERTIFICATION_IDENTIFIER;
import static com.frost.bfriend.common.constants.CookieConstants.SMS_CERTIFICATION_IDENTIFIER;
import static com.frost.bfriend.common.constants.RegexConstants.*;
import static com.frost.bfriend.dto.UserDto.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Validated
public class UserController {

    private final UserService userService;
    private final CookieHandler cookieHandler;

    @GetMapping("/email/{email}")
    public ResponseEntity<Void> isEmailDuplicated(
            @PathVariable @Email(message = EMAIL_REGEX_FAIL) String email) {
        userService.isEmailDuplicated(email);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/phone/{phone}")
    public ResponseEntity<Void> isPhoneDuplicated(
            @PathVariable @Pattern(regexp = PHONE, message = PHONE_REGEX_FAIL) String phone) {
        userService.isPhoneDuplicated(phone);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/email/certification/{email}")
    public ResponseEntity<Void> sendCertificationEmail(
            @PathVariable @Email(message = EMAIL_REGEX_FAIL) String email) {
        userService.sendCertificationEmail(email);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/email/certification")
    public ResponseEntity<Void> checkEmailCertificationCode(@RequestBody @Valid EmailCertificationRequest request) {
        String identifier = userService.checkEmailCertificationCode(request);
        ResponseCookie emailIdentifierCookie = cookieHandler.createEmailIdentifierCookie(identifier);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, emailIdentifierCookie.toString()).build();
    }

    @GetMapping("/phone/certification/{phone}")
    public ResponseEntity<Void> sendCertificationSms(
            @PathVariable @Pattern(regexp = PHONE, message = PHONE_REGEX_FAIL) String phone) {
        userService.sendCertificationSms(phone);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/phone/certification")
    public ResponseEntity<Void> checkSmsCertificationCode(
            @RequestBody @Valid SmsCertificationRequest request) {
        String identifier = userService.checkSmsCertificationCode(request);
        ResponseCookie smsIdentifierCookie = cookieHandler.createSmsIdentifierCookie(identifier);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, smsIdentifierCookie.toString()).build();
    }

    @PostMapping
    public ResponseEntity<Void> saveUser(
            @RequestBody @Valid SaveRequest request,
            @CookieValue(value = EMAIL_CERTIFICATION_IDENTIFIER) Cookie emailIdentifierCookie,
            @CookieValue(value = SMS_CERTIFICATION_IDENTIFIER) Cookie smsIdentifierCookie) {
        userService.saveUser(request, emailIdentifierCookie.getValue(), smsIdentifierCookie.getValue());
        ResponseCookie deletedEmailCookie = cookieHandler.expireCookie(EMAIL_CERTIFICATION_IDENTIFIER);
        ResponseCookie deletedSmsCookie = cookieHandler.expireCookie(SMS_CERTIFICATION_IDENTIFIER);

        return ResponseEntity.ok().header(
                HttpHeaders.SET_COOKIE, deletedEmailCookie.toString(), deletedSmsCookie.toString()).build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginRequest request) {
        TokenAndName tokenAndName = userService.login(request);
        ResponseCookie accessTokenCookie = cookieHandler.createAccessTokenCookie(tokenAndName.getToken());
        String name = tokenAndName.getName();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString()).body(name);
    }

    @CheckUser
    @GetMapping("/logout")
    public ResponseEntity<Void> logout() {
        ResponseCookie expireAccessTokenCookie = cookieHandler.expireCookie(CookieConstants.ACCESS_TOKEN);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, expireAccessTokenCookie.toString()).build();
    }

    @PatchMapping("/temporary-password")
    public ResponseEntity<Void> issueTemporaryPassword(
            @RequestBody @Valid TemporaryPasswordRequest request) {
        userService.issueTemporaryPassword(request);

        return ResponseEntity.ok().build();
    }

    @CheckUser
    @PatchMapping("/password")
    public ResponseEntity<Void> updatePassword(
            @LoginUser Long userId, @RequestBody @Valid UpdatePasswordRequest request) {
        userService.updatePassword(userId, request);

        return ResponseEntity.ok().build();
    }

    @CheckUser
    @DeleteMapping
    public ResponseEntity<Void> deleteUser(
            @LoginUser Long userId, @RequestBody @Valid DeleteRequest request) {
        userService.deleteUser(userId, request);

        return ResponseEntity.ok().build();
    }

    @CheckUser
    @GetMapping("/mypage")
    public ResponseEntity<UserResponseForMyPage> getMyInformation(@LoginUser Long userId) {
        return ResponseEntity.ok(userService.getMyInformation(userId));
    }
}