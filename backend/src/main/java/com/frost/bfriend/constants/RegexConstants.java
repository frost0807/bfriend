package com.frost.bfriend.constants;

public class RegexConstants {
    /**
     * PASSWORD 는 영어, 숫자, 특수문자를 섞어서
     */

    public static final String PHONE = "^(01[016789]\\d{3,4}\\d{4})$";
    public static final String PASSWORD = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,20}$";
    public static final String EMAIL_CERTIFICATION = "^[0-9a-zA-Z]{8}$";
    public static final String SMS_CERTIFICATION = "^[0-9a-zA-Z]{8}$";
}