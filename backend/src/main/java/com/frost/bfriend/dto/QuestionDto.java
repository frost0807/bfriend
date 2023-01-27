package com.frost.bfriend.dto;

import com.frost.bfriend.entity.Question;
import lombok.Getter;

@Getter
public class QuestionDto {
    private Integer id;

    private String content;

    public QuestionDto(Question question) {
        this.id = question.getId();
        this.content = question.getContent();
    }
}
