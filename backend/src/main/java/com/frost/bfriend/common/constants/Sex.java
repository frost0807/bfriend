package com.frost.bfriend.common.constants;

public enum Sex {
    MALE("남성"), FEMALE("여성");
    private String name;

    Sex(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
