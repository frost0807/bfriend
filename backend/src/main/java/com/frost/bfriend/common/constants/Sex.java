package com.frost.bfriend.common.constants;

public enum Sex {
    MALE("남자"), FEMALE("여자");
    private String name;

    Sex(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
