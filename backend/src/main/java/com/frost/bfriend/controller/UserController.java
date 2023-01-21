package com.frost.bfriend.controller;

import com.frost.bfriend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.frost.bfriend.constants.ResponseConstants.OK;
import static com.frost.bfriend.dto.UserDto.*;
import static com.frost.bfriend.dto.UserDto.SaveRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

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
        userService.checkEmailCertificationCode(request);
        return OK;
    }
}