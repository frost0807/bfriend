package com.frost.bfriend.service;

import com.frost.bfriend.dto.QuestionAnswerDto;
import com.frost.bfriend.dto.QuestionAnswerDto.SaveRequest;
import com.frost.bfriend.entity.*;
import com.frost.bfriend.exception.questionanswer.AnswerNotFoundException;
import com.frost.bfriend.exception.questionanswer.QuestionCategoryNotFoundException;
import com.frost.bfriend.exception.questionanswer.QuestionNotFoundException;
import com.frost.bfriend.exception.user.UserNotFoundException;
import com.frost.bfriend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.frost.bfriend.dto.QuestionAnswerDto.*;

@Service
@RequiredArgsConstructor
public class QuestionAnswerService {

    private final UserRepository userRepository;
    private final QuestionCategoryRepository questionCategoryRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final QuestionAnswerRepository questionAnswerRepository;

    @Transactional(readOnly = true)
    public List<QuestionCategoryDto> getQuestionCategories() {
        List<QuestionCategory> questionCategories = questionCategoryRepository.findAll();

        return questionCategories.stream()
                .map(questionCategory -> new QuestionCategoryDto(questionCategory))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<QuestionDto> getQuestionsByCategoryId(Integer categoryId) {
        QuestionCategory questionCategory = questionCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new QuestionCategoryNotFoundException());

        return questionCategory.getQuestions()
                .stream()
                .map(question -> new QuestionDto(question))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AnswerDto> getAnswersByQuestionId(Integer questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionNotFoundException());

        return question.getAnswers()
                .stream()
                .map(answer -> new AnswerDto(answer))
                .collect(Collectors.toList());
    }

    @Transactional
    public void saveQuestionAnswer(Long userId, SaveRequest request) {
        QuestionAnswer questionAnswer =
                createQuestionAnswer(getUserById(userId), getQuestionById(request), getAnswerById(request));
        questionAnswerRepository.save(questionAnswer);
    }

    @Transactional(readOnly = true)
    public List<QuestionAnswerResponse> getQuestionAnswers(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("해당 유저가 존재하지 않습니다."));

        return user.getQuestionAnswers()
                .stream()
                .map(questionAnswer -> new QuestionAnswerResponse(questionAnswer))
                .collect(Collectors.toList());
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("해당 유저가 존재하지 않습니다."));
    }

    private Question getQuestionById(SaveRequest request) {
        return questionRepository.findById(request.getQuestionId()).orElseThrow(() -> new QuestionNotFoundException());
    }

    private Answer getAnswerById(SaveRequest request) {
        return answerRepository.findById(request.getAnswerId()).orElseThrow(() -> new AnswerNotFoundException());
    }

    private QuestionAnswer createQuestionAnswer(User user, Question question, Answer answer) {
        return QuestionAnswer.builder()
                .user(user)
                .question(question)
                .answer(answer)
                .build();
    }

}