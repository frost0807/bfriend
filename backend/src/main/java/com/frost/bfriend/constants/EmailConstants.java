package com.frost.bfriend.constants;


public class EmailConstants {

    public static final String EMAIL_FROM = "junesuck99@gmail.com";

    public static final String CERTIFICATION_TITLE =
            "BFriend 회원가입을 위한 인증 메일입니다.";

    public static final String CERTIFICATION_CONTENT =
            "아래 인증코드를 회원가입 인증코드 입력창에 입력해주세요\n\n";

    public static final String CERTIFICATION_CODE = "email certification code:";

    public static final String CERTIFICATION_IDENTIFIER = "email certification identifier:";

    public static final long CERTIFICATION_CODE_DURATION = 60 * 5;

    public static final long EMAIL_CERTIFICATION_IDENTIFIER_DURATION = 60 * 60;

    public static final String NEW_PASSWORD_TITLE =
            "BFriend 새로운 비밀번호 안내를 위한 메일입니다.";

    public static final String NEW_PASSWORD_CONTENT =
            "아래 새로운 비밀번호를 로그인 화면에서 입력해주세요\n\n";

    public static final char[] SPECIAL_SYMBOL_LIST =
            {'!', '@', '#', '$', '%', '^', '&', '*'};
}
