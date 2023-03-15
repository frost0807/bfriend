package com.frost.bfriend.controller;

import com.frost.bfriend.common.annotation.CheckUser;
import com.frost.bfriend.common.annotation.LoginUser;
import com.frost.bfriend.service.QuestionAnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.frost.bfriend.dto.QuestionAnswerDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/question-answers")
public class QuestionAnswerController {

    private final QuestionAnswerService questionAnswerService;

    @GetMapping("/categories-and-questions")
    public ResponseEntity<CategoryAndQuestionResponse> getCategoriesAndQuestions() {
        return ResponseEntity.ok(questionAnswerService.getCategoriesAndQuestions());
    }

    @CheckUser
    @PostMapping
    public ResponseEntity<Void> saveQuestionAnswer(
            @LoginUser Long userId,
            @RequestBody @Valid List<SaveRequest> requests) {
        questionAnswerService.saveQuestionAnswer(userId, requests);

        return ResponseEntity.ok().build();
    }

    @CheckUser
    @PatchMapping
    public ResponseEntity<Void> updateQuestionAnswer(
            @LoginUser Long userId,
            @RequestBody @Valid List<UpdateRequest> requests) {
        questionAnswerService.updateQuestionAnswer(userId, requests);

        return ResponseEntity.ok().build();
    }

    @CheckUser
    @GetMapping
    public ResponseEntity<List<ResponseForUpdate>> getQuestionAnswersForUpdate(
            @LoginUser Long userId) {
        return ResponseEntity.ok(questionAnswerService.getQuestionAnswersForUpdateByUserId(userId));
    }

    @CheckUser
    @GetMapping("/mypage")
    public ResponseEntity<List<QuestionAnswerResponseForMyPage>> getQuestionAnswersForMyPage(
            @LoginUser Long userId) {
        return ResponseEntity.ok(questionAnswerService.getQuestionAnswersForMyPageByUserId(userId));
    }
}
