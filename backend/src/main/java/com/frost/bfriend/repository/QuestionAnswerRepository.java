package com.frost.bfriend.repository;

import com.frost.bfriend.entity.QuestionAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuestionAnswerRepository extends JpaRepository<QuestionAnswer, Long> {
}