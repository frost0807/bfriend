package com.frost.bfriend.controller;

import com.frost.bfriend.dto.AnswerDto;
import com.frost.bfriend.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/answers")
public class AnswerController {

    private final AnswerService answerService;

    @GetMapping
    public ResponseEntity<List<AnswerDto>> getAnswers(@RequestParam Integer questionId) {
        return ResponseEntity.ok(answerService.getAnswers(questionId));
    }
}
