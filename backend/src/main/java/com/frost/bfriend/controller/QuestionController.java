package com.frost.bfriend.controller;

import com.frost.bfriend.dto.QuestionDto;
import com.frost.bfriend.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping
    public ResponseEntity<List<QuestionDto>> getQuestions(@RequestParam Integer categoryId) {
        return ResponseEntity.ok(questionService.getQuestions(categoryId));
    }
}
