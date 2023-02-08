package com.frost.bfriend.common.constants;

public enum Region {
    SEOUL("서울"),
    GYEONGGI("경기"),
    BUSAN("부산"),
    DAEGU("대구"),
    INCHEON("인천"),
    GWANGJU("광주"),
    DAEJEON("대전"),
    ULSAN("울산"),
    SEJONG("세종");
    private String name;

    Region(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}