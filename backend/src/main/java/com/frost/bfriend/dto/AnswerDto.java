package com.frost.bfriend.dto;

import com.frost.bfriend.entity.Answer;
import lombok.Getter;

@Getter
public class AnswerDto {

    private Integer id;

    private String content;

    public AnswerDto(Answer answer) {
        this.id = answer.getId();
        this.content = answer.getContent();
    }
}
