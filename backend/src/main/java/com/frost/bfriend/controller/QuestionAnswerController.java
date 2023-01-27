package com.frost.bfriend.controller;

import com.frost.bfriend.dto.AnswerDto;
import com.frost.bfriend.dto.QuestionCategoryDto;
import com.frost.bfriend.dto.QuestionDto;
import com.frost.bfriend.service.AnswerService;
import com.frost.bfriend.service.QuestionCategoryService;
import com.frost.bfriend.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class QuestionAnswerController {

    private final QuestionCategoryService questionCategoryService;
    private final QuestionService questionService;
    private final AnswerService answerService;

    @GetMapping("/question-categories")
    public ResponseEntity<List<QuestionCategoryDto>> getQuestionCategories() {
        return ResponseEntity.ok(questionCategoryService.getQuestionCategories());
    }

    @GetMapping("/questions")
    public ResponseEntity<List<QuestionDto>> getQuestions(@RequestParam Integer categoryId) {
        return ResponseEntity.ok(questionService.getQuestions(categoryId));
    }

    @GetMapping("/answers")
    public ResponseEntity<List<AnswerDto>> getAnswers(@RequestParam Integer questionId) {
        return ResponseEntity.ok(answerService.getAnswers(questionId));
    }
}
