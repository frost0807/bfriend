package com.frost.bfriend.repository;

import com.frost.bfriend.entity.QQuestion;
import com.frost.bfriend.entity.QQuestionAnswer;
import com.frost.bfriend.entity.QuestionAnswer;
import com.frost.bfriend.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.EntityManager;
import java.util.List;

import static com.frost.bfriend.entity.QQuestion.*;
import static com.frost.bfriend.entity.QQuestionAnswer.*;

public class QuestionAnswerRepositoryImpl implements QuestionAnswerRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public QuestionAnswerRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<QuestionAnswer> findAllByUser(User user) {
        return queryFactory
                .selectFrom(questionAnswer)
                .join(questionAnswer.question, question)
                .fetchJoin()
                .where(questionAnswer.user.eq(user))
                .orderBy(questionAnswer.question.questionCategory.id.asc())
                .distinct()
                .fetch();
    }
}
