package com.frost.bfriend.dto;

import com.frost.bfriend.entity.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

public class QuestionAnswerDto {
    @Getter
    public static class CategoryDto {
        private Integer questionCategoryId;

        private String name;

        public CategoryDto(QuestionCategory questionCategory) {
            this.questionCategoryId = questionCategory.getId();
            this.name = questionCategory.getName().getName();
        }
    }

    @Getter
    public static class QuestionDto {
        private Integer questionId;

        private Integer categoryId;

        private String content;

        public QuestionDto(Question question) {
            this.categoryId = question.getQuestionCategory().getId();
            this.questionId = question.getId();
            this.content = question.getContent();
        }
    }

    @Getter
    public static class CategoryAndQuestionResponse {
        private List<CategoryDto> categories;

        private List<QuestionDto> questions;

        public CategoryAndQuestionResponse(List<CategoryDto> categories, List<QuestionDto> questions) {
            this.categories = categories;
            this.questions = questions;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class SaveRequest {
        @NotBlank
        private Integer questionId;

        @NotBlank(message = "답변을 입력해주세요")
        @Size(max = 100, message = "최대 50자까지만 입력해주세요")
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
    public static class ResponseForMyPage {
        private String question;

        private String answer;

        public ResponseForMyPage(QuestionAnswer questionAnswer) {
            this.question = questionAnswer.getQuestion().getContent();
            this.answer = questionAnswer.getAnswer();
        }
    }

    @Getter
    public static class ResponseForUpdate {
        private Long questionAnswerId;

        private Integer categoryId;

        private Integer questionId;

        private String answer;

        public ResponseForUpdate(QuestionAnswer questionAnswer) {
            this.questionAnswerId = questionAnswer.getId();
            this.categoryId = questionAnswer.getQuestion().getQuestionCategory().getId();
            this.questionId = questionAnswer.getQuestion().getId();
            this.answer = questionAnswer.getAnswer();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UpdateRequest {
        @NotBlank
        private Long questionAnswerId;

        @NotBlank
        private Integer questionId;

        @NotBlank(message = "답변을 입력해주세요")
        @Size(max = 100, message = "최대 50자까지만 입력해주세요")
        private String answer;

        public QuestionAnswer toEntity(User user, Question question) {
            return QuestionAnswer.builder()
                    .id(this.questionAnswerId)
                    .user(user)
                    .question(question)
                    .answer(this.answer)
                    .build();
        }
    }
}
