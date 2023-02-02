package com.frost.bfriend.common.constants;

public class SmsConstants {
    public static final String SMS_FROM = "01097059896";

    public static final String CERTIFICATION_CONTENT =
            "BFriend 회원가입을 위해 아래의 인증코드를 인증코드 입력창에 입력해주세요\n\n";

    public static final String STATUS_ACCEPTED = "2000";

    public static final String CERTIFICATION_CODE = "sms certification:";

    public static final String CERTIFICATION_IDENTIFIER = "sms certification identifier:";

    public static final long CERTIFICATION_CODE_DURATION = 60 * 5;

    public static final long SMS_CERTIFICATION_IDENTIFIER_EXPIRY_SECONDS = 60 * 60;
}