package com.frost.bfriend.common.constants;

public enum QuestionCategoryName {
    PERSONALITY("성격"),
    LIKE("선호"),
    DISLIKE("비선호"),
    HOBBY("취미"),
    ETC("기타");

    private String name;

    QuestionCategoryName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
