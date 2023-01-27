package com.frost.bfriend.service;

import com.frost.bfriend.dto.QuestionDto;
import com.frost.bfriend.entity.Question;
import com.frost.bfriend.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    public List<QuestionDto> getQuestions(Integer categoryId) {
        List<Question> questions = questionRepository.findAllByQuestionCategoryId(categoryId);

        return questions.stream()
                .map(question -> new QuestionDto(question))
                .collect(Collectors.toList());
    }

}
