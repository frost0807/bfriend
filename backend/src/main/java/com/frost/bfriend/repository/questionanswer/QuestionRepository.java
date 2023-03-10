package com.frost.bfriend.repository.questionanswer;

import com.frost.bfriend.entity.Question;
import com.frost.bfriend.entity.QuestionCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
}
