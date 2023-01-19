package com.frost.bfriend.dto;

import com.frost.bfriend.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.frost.bfriend.constants.RegexConstants.PASSWORD;
import static com.frost.bfriend.constants.RegexConstants.PHONE;

public class UserDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class CreateRequest {
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
