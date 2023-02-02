package com.frost.bfriend.dto;

import com.frost.bfriend.entity.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

public class QuestionAnswerDto {
    @Getter
    public static class QuestionCategoryDto {
        private Integer questionCategoryId;

        private String name;

        public QuestionCategoryDto(QuestionCategory questionCategory) {
            this.questionCategoryId = questionCategory.getId();
            this.name = questionCategory.getName().getName();
        }
    }

    @Getter
    public static class QuestionDto {
        private Integer questionId;

        private String content;

        public QuestionDto(Question question) {
            this.questionId = question.getId();
            this.content = question.getContent();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class SaveRequest {
        @NotBlank
        private Integer questionId;

        @NotBlank
        private String answer;

        public QuestionAnswer toEntity(User user, Question question) {
            return QuestionAnswer.builder()
                    .user(user)
                    .question(question)
                    .answer(this.answer)
                    .build();
        }
    }

    @Getter
    public static class QuestionAnswerResponse {

        private Long questionAnswerId;

        private String question;

        private String answer;

        public QuestionAnswerResponse(QuestionAnswer questionAnswer) {
            this.questionAnswerId = questionAnswer.getId();
            this.question = questionAnswer.getQuestion().getContent();
            this.answer = questionAnswer.getAnswer();
        }
    }
}
