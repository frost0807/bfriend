package com.frost.bfriend.repository.questionanswer;

import com.frost.bfriend.entity.QuestionAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionAnswerRepository extends JpaRepository<QuestionAnswer, Long>, QuestionAnswerRepositoryCustom {
}