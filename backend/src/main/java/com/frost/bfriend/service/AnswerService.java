package com.frost.bfriend.service;

import com.frost.bfriend.dto.AnswerDto;
import com.frost.bfriend.entity.Answer;
import com.frost.bfriend.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;

    public List<AnswerDto> getAnswers(Integer questionId) {
        List<Answer> answers = answerRepository.findAllByQuestionId(questionId);

        return answers.stream()
                .map(answer -> new AnswerDto(answer))
                .collect(Collectors.toList());
    }
}
