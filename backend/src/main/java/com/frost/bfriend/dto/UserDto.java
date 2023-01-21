package com.frost.bfriend.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static com.frost.bfriend.constants.RegexConstants.*;

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
        private String region;
        
        @NotBlank(message = "출생년도를 입력해주세요")
        private String birthday;
        
        @NotBlank(message = "성별을 입력해주세요")
        private String sex;
    }
}
