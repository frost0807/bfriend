package com.frost.bfriend.common.constants;

public class RegexConstants {
    /**
     * PASSWORD 는 영어, 숫자, 특수문자를 섞어서 8 - 20자
     */

    public static final String PHONE = "^(01[016789]\\d{3,4}\\d{4})$";
    public static final String PASSWORD = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,20}$";
    public static final String EMAIL_CERTIFICATION = "^[0-9a-zA-Z]{8}$";
    public static final String SMS_CERTIFICATION = "^[0-9a-zA-Z]{8}$";

    public static final String EMAIL_REGEX_FAIL = "올바른 이메일 주소를 입력해주세요";

    public static final String PHONE_REGEX_FAIL = "올바른 휴대폰 번호를 입력해주세요";
}