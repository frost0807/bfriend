package com.frost.bfriend.repository.questionanswer;

import com.frost.bfriend.entity.QuestionAnswer;
import com.frost.bfriend.entity.User;

import java.util.List;

public interface QuestionAnswerRepositoryCustom {
    List<QuestionAnswer> findAllByUser(User user);
}
