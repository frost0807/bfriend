package com.frost.bfriend.dto;

import com.frost.bfriend.entity.QuestionCategory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionCategoryDto {
    private Long id;

    private String name;

    public QuestionCategoryDto(QuestionCategory questionCategory) {
        this.id = questionCategory.getId();
        this.name = questionCategory.getName().getName();
    }
}
