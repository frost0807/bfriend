package com.frost.bfriend.common;

import com.frost.bfriend.common.constants.QuestionCategoryName;
import com.frost.bfriend.common.constants.Region;
import com.frost.bfriend.common.constants.Sex;
import com.frost.bfriend.common.constants.UserLevel;
import com.frost.bfriend.common.util.encryption.EncryptionService;
import com.frost.bfriend.dto.UserDto;
import com.frost.bfriend.entity.Answer;
import com.frost.bfriend.entity.Question;
import com.frost.bfriend.entity.QuestionCategory;
import com.frost.bfriend.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static com.frost.bfriend.dto.UserDto.*;

@Profile("local")
@Component
@RequiredArgsConstructor
public class init {

    private final InitService initService;


    @PostConstruct
    public void init() {
        initService.initQuestionAnswer();
        initService.initUser();
    }

    @RequiredArgsConstructor
    @Component
    static class InitService {
        @PersistenceContext
        private EntityManager em;


        private final EncryptionService encryptionService;

        @Transactional
        public void initUser() {
            SaveRequest userDto = SaveRequest.builder()
                    .email("junesuck99@naver.com")
                    .phone("01097059896")
                    .password("1234qwer!")
                    .name("최준석")
                    .sex(Sex.MALE)
                    .region(Region.SEOUL)
                    .birthday(LocalDate.now())
                    .build();
            userDto.encryptPassword(encryptionService);
            em.persist(userDto.toEntity());
        }

        @Transactional
        public void initQuestionAnswer() {
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
