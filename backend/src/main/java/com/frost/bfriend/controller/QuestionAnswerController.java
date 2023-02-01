package com.frost.bfriend.controller;

import com.frost.bfriend.common.annotation.LoginUser;
import com.frost.bfriend.service.QuestionAnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.frost.bfriend.dto.QuestionAnswerDto.*;

@RestController
@RequiredArgsConstructor
public class QuestionAnswerController {

    private final QuestionAnswerService questionAnswerService;

    @GetMapping("/question-categories")
    public ResponseEntity<List<QuestionCategoryDto>> getQuestionCategories() {
        return ResponseEntity.ok(questionAnswerService.getQuestionCategories());
    }

    @GetMapping("/questions")
    public ResponseEntity<List<QuestionDto>> getQuestions(@RequestParam Integer categoryId) {
        return ResponseEntity.ok(questionAnswerService.getQuestionsByCategoryId(categoryId));
    }

    @GetMapping("/answers")
    public ResponseEntity<List<AnswerDto>> getAnswers(@RequestParam Integer questionId) {
        return ResponseEntity.ok(questionAnswerService.getAnswersByQuestionId(questionId));
    }

    @PostMapping("/question-answers")
    public ResponseEntity<Void> saveQuestionAnswer(@LoginUser Long userId, SaveRequest request) {
        questionAnswerService.saveQuestionAnswer(userId, request);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/question-answers")
    public ResponseEntity<List<QuestionAnswerResponse>> getQuestionAnswers(@LoginUser Long userId) {
        return ResponseEntity.ok(questionAnswerService.getQuestionAnswers(userId));
    }
}
