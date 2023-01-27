package com.frost.bfriend.service;

import com.frost.bfriend.dto.QuestionCategoryDto;
import com.frost.bfriend.entity.QuestionCategory;
import com.frost.bfriend.repository.QuestionCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionCategoryService {

    private final QuestionCategoryRepository questionCategoryRepository;

    public List<QuestionCategoryDto> getQuestionCategories() {
        List<QuestionCategory> questionCategories = questionCategoryRepository.findAll();

        return questionCategories.stream()
                .map(questionCategory -> new QuestionCategoryDto(questionCategory))
                .collect(Collectors.toList());
    }
}