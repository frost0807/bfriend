package com.frost.bfriend.controller;

import com.frost.bfriend.dto.QuestionCategoryDto;
import com.frost.bfriend.service.QuestionCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/question-categories")
public class QuestionCategoryController {

    private final QuestionCategoryService questionCategoryService;

    @GetMapping
    public ResponseEntity<List<QuestionCategoryDto>> getQuestionCategories() {
        return ResponseEntity.ok(questionCategoryService.getQuestionCategories());
    }
}