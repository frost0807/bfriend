package com.frost.bfriend.dto;

import com.frost.bfriend.entity.QuestionCategory;
import lombok.Getter;

@Getter
public class QuestionCategoryDto {
    private Integer id;

    private String name;

    public QuestionCategoryDto(QuestionCategory questionCategory) {
        this.id = questionCategory.getId();
        this.name = questionCategory.getName().getName();
    }
}
