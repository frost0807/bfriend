package com.frost.bfriend.common;

import com.frost.bfriend.common.constants.QuestionCategoryName;
import com.frost.bfriend.entity.Answer;
import com.frost.bfriend.entity.Question;
import com.frost.bfriend.entity.QuestionCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;

@Profile("local")
@Component
@RequiredArgsConstructor
public class init {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.init();
    }

    @Component
    static class InitService {
        @PersistenceContext
        private EntityManager em;

        @Transactional
        public void init() {
            QuestionCategory personality = QuestionCategory.builder().name(QuestionCategoryName.PERSONALITY).build();
            QuestionCategory like = QuestionCategory.builder().name(QuestionCategoryName.LIKE).build();
            QuestionCategory dislike = QuestionCategory.builder().name(QuestionCategoryName.DISLIKE).build();
            QuestionCategory hobby = QuestionCategory.builder().name(QuestionCategoryName.HOBBY).build();
            QuestionCategory etc = QuestionCategory.builder().name(QuestionCategoryName.ETC).build();
            List<QuestionCategory> questionCategories = Arrays.asList(personality, like, dislike, hobby, etc);
            em.persist(personality);
            em.persist(like);
            em.persist(dislike);
            em.persist(hobby);
            em.persist(etc);

            for (int i = 0; i < 5; i++) {
                for (int j = 1; j < 6; j++) {
                    Question question = Question.builder()
                            .questionCategory(questionCategories.get(i))
                            .content(questionCategories.get(i).getName().getName() + "질문 " + j)
                            .build();
                    em.persist(question);
                    for (int k = 1; k < 11; k++) {
                        Answer answer = Answer.builder()
                                .question(question)
                                .content(question.getContent() + " 답변 " + k)
                                .build();
                        em.persist(answer);
                    }
                }
            }
        }
    }
}
