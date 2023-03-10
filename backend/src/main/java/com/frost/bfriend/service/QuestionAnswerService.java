package com.frost.bfriend.service;

import com.frost.bfriend.entity.Question;
import com.frost.bfriend.entity.QuestionAnswer;
import com.frost.bfriend.entity.QuestionCategory;
import com.frost.bfriend.entity.User;
import com.frost.bfriend.exception.questionanswer.QuestionNotFoundException;
import com.frost.bfriend.exception.user.UserNotFoundException;
import com.frost.bfriend.repository.questionanswer.QuestionAnswerRepository;
import com.frost.bfriend.repository.questionanswer.QuestionCategoryRepository;
import com.frost.bfriend.repository.questionanswer.QuestionRepository;
import com.frost.bfriend.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.frost.bfriend.dto.QuestionAnswerDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionAnswerService {

    private final UserRepository userRepository;
    private final QuestionCategoryRepository questionCategoryRepository;
    private final QuestionRepository questionRepository;
    private final QuestionAnswerRepository questionAnswerRepository;

    @Transactional(readOnly = true)
    public CategoryAndQuestionResponse getCategoriesAndQuestions() {
        List<CategoryDto> categoryDtos = getAllCategoryDtos();
        List<QuestionDto> questionDtos = getAllQuestionDtos();

        return new CategoryAndQuestionResponse(categoryDtos, questionDtos);
    }

    private List<CategoryDto> getAllCategoryDtos() {
        List<QuestionCategory> questionCategories = questionCategoryRepository.findAll();
        List<CategoryDto> categoryDtos = questionCategories.stream()
                .map(questionCategory -> new CategoryDto(questionCategory))
                .collect(Collectors.toList());
        return categoryDtos;
    }

    private List<QuestionDto> getAllQuestionDtos() {
        List<Question> questions = questionRepository.findAll();
        List<QuestionDto> questionDtos = questions.stream()
                .map(question -> new QuestionDto(question))
                .collect(Collectors.toList());
        return questionDtos;
    }

    @Transactional
    public void saveQuestionAnswer(Long userId, List<SaveRequest> requestDtos) {
        User user = getUserById(userId);
        for (SaveRequest requestDto : requestDtos) {
            QuestionAnswer questionAnswer =
                    requestDto.toEntity(user, getQuestionById(requestDto.getQuestionId()));
            questionAnswerRepository.save(questionAnswer);
        }
    }

    @Transactional(readOnly = true)
    public List<ResponseForUpdate> getQuestionAnswersForUpdateByUserId(Long userId) {
        User user = getUserById(userId);
        List<QuestionAnswer> questionAnswers = questionAnswerRepository.findAllByUser(user);

        return questionAnswers.stream()
                .map(questionAnswer -> new ResponseForUpdate(questionAnswer))
                .collect(Collectors.toList());
    }

    private User getUserById(Long userId) {
        return userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new UserNotFoundException("?????? ????????? ???????????? ????????????."));
    }

    private Question getQuestionById(Integer questionId) {
        return questionRepository.findById(questionId).orElseThrow(() -> new QuestionNotFoundException());
    }

    @Transactional(readOnly = true)
    public List<QuestionAnswerResponseForMyPage> getQuestionAnswersForMyPageByUserId(Long userId) {
        User user = getUserById(userId);
        List<QuestionAnswer> questionAnswers = questionAnswerRepository.findAllByUser(user);

        return questionAnswers.stream()
                .map(questionAnswer -> new QuestionAnswerResponseForMyPage(questionAnswer))
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateQuestionAnswer(Long userId, List<UpdateRequest> requestDtos) {
        User user = getUserById(userId);
        //????????? ????????? ????????? ?????????
        List<QuestionAnswer> originalQuestionAnswers = questionAnswerRepository.findAllByUser(user);
        //????????? ????????? ??? ???????????? ??? ????????? filter ??? ??????
        originalQuestionAnswers.stream()
                .filter(original -> requestDtos.stream().noneMatch(
                        updating -> updating.getQuestionAnswerId() == original.getId()))
                .forEach(originalToDelete -> questionAnswerRepository.delete(originalToDelete));
        for (UpdateRequest requestDto : requestDtos) {
            QuestionAnswer questionAnswer =
                    requestDto.toEntity(user, getQuestionById(requestDto.getQuestionId()));
            questionAnswerRepository.save(questionAnswer);
        }
    }
}