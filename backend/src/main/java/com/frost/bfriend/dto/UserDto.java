package com.frost.bfriend.dto;

import com.frost.bfriend.common.constants.Region;
import com.frost.bfriend.common.constants.Sex;
import com.frost.bfriend.common.constants.UserLevel;
import com.frost.bfriend.entity.User;
import com.frost.bfriend.common.util.encryption.EncryptionService;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import java.time.LocalDate;

import static com.frost.bfriend.common.constants.RegexConstants.*;

public class UserDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class EmailCertificationRequest {
        @NotBlank(message = "이메일 주소를 입력해주세요")
        @Email(message = "이메일 주소 형식에 맞게 입력해주세요")
        private String email;

        @NotBlank(message = "이메일 인증 코드를 입력해주세요")
        @Pattern(regexp = EMAIL_CERTIFICATION, message = "올바른 이메일 인증 코드를 입력해주세요")
        private String certificationCode;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class SmsCertificationRequest {
        @NotBlank(message = "이메일 주소를 입력해주세요")
        @Pattern(regexp = PHONE, message = "휴대폰 번호 형식에 맞게 입력해주세요")
        private String phone;

        @NotBlank(message = "SMS 인증 코드를 입력해주세요")
        @Pattern(regexp = SMS_CERTIFICATION, message = "올바른 SMS 인증 코드를 입력해주세요")
        private String certificationCode;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class SaveRequest {
        @NotBlank(message = "이메일 주소를 입력해주세요")
        @Email(message = "이메일 주소 형식에 맞게 입력해주세요")
        private String email;

        @NotBlank(message = "비밀번호를 입력해주세요")
        @Size(min = 8, max = 20, message = "8자 이상 20자 이하의 비밀번호를 입력해주세요")
        @Pattern(regexp = PASSWORD, message = "숫자, 영어, 특수문자를 최소 1글자씩 포함시켜 입력해주세요")
        private String password;

        @NotBlank(message = "이름을 입력해주세요")
        @Size(min = 2, message = "2자 이상의 정확한 이름을 입력해주세요")
        private String name;

        @NotBlank(message = "휴대폰 번호를 입력해주세요")
        @Pattern(regexp = PHONE, message = "휴대폰 번호 형식에 맞게 입력해주세요")
        private String phone;

        @NotBlank(message = "거주지역을 입력해주세요")
        private Region region;

        @NotBlank(message = "출생년월일을 입력해주세요")
        private LocalDate birthday;

        @NotBlank(message = "성별을 입력해주세요")
        private Sex sex;

        public void encryptPassword(EncryptionService encryptionService) {
            this.password = encryptionService.encrypt(password);
        }

        public User toEntity() {
            return User.builder()
                    .email(this.email)
                    .password(this.password)
                    .level(UserLevel.NORMAL)
                    .name(this.name)
                    .phone(this.phone)
                    .region(this.region)
                    .birthday(this.birthday)
                    .sex(this.sex)
                    .activityPoint(0)
                    .isSuspended(false)
                    .isDeleted(false)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class LoginRequest {
        @NotBlank(message = "이메일 주소를 입력해주세요")
        @Email(message = "이메일 주소 형식에 맞게 입력해주세요")
        private String email;

        @NotBlank(message = "비밀번호를 입력해주세요")
        private String password;

        public boolean isPasswordCorrect(EncryptionService encryptionService, String encryptedPassword) {
            return encryptionService.isSamePassword(this.password, encryptedPassword);
        }
    }

    @Getter
    public static class LoginResponse {
        private String name;

        private int activityPoint;

        public LoginResponse(User user) {
            this.name = user.getName();
            this.activityPoint = user.getActivityPoint();
        }
    }

    @Getter
    @AllArgsConstructor
    public static class TokenAndName {
        private String token;

        private String name;
    }

    @Getter
    public static class UserIdAndLevel {
        private Long userId;

        private UserLevel userLevel;

        public UserIdAndLevel(String userId, String userLevel) {
            this.userId = Long.parseLong(userId);
            this.userLevel = UserLevel.valueOf(userLevel);
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class TemporaryPasswordRequest {
        @NotBlank(message = "이메일을 입력해주세요")
        private String email;

        @NotBlank(message = "이름을 입력해주세요")
        private String name;
    }
    
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UpdatePasswordRequest {
        @NotBlank(message = "기존 비밀번호를 입력해주세요")
        private String originalPassword;
        
        @NotBlank(message = "새 비밀번호를 입력해주세요")
        @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요")
        @Pattern(regexp = PASSWORD, message = "숫자, 문자, 특수문자 3가지를 조합해 입력해주세요")
        private String newPassword;

        public boolean checkPassword(EncryptionService encryptionService, String encryptedPassword) {
            return encryptionService.isSamePassword(this.originalPassword, encryptedPassword);
        }

        public boolean isAlreadyMyPassword() {
            return originalPassword.equals(newPassword);
        }

        public void encryptPassword(EncryptionService encryptionService) {
            this.newPassword = encryptionService.encrypt(newPassword);
        }
    }
}