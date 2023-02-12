package com.frost.bfriend.common;

import com.frost.bfriend.common.constants.*;
import com.frost.bfriend.common.util.encryption.EncryptionService;
import com.frost.bfriend.entity.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static com.frost.bfriend.dto.UserDto.SaveRequest;

@Slf4j
@Profile("local")
@Component
@RequiredArgsConstructor
public class init {

    private final InitService initService;


    @PostConstruct
    @Transactional
    public void init() {
        initService.initUser();
        initService.initQuestionAnswer();
        initService.initMatchPost();
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

            em.flush();
            em.clear();
            for (int i = 0; i < 5; i++) {
                for (int j = 1; j < 6; j++) {
                    Question question = Question.builder()
                            .questionCategory(questionCategories.get(i))
                            .content(questionCategories.get(i).getName().getName() + "질문 " + j)
                            .build();
                    em.persist(question);
                }
            }
            em.flush();
            em.clear();

            for (int i = 1; i < 25; i += 2) {
                log.info("i = {}", i);
                QuestionAnswer questionAnswer = QuestionAnswer.builder()
                        .user(em.find(User.class, 1L))
                        .question(em.find(Question.class, i))
                        .answer("답변 " + i)
                        .build();
                em.persist(questionAnswer);
            }
        }

        @Transactional
        public void initMatchPost() {

            for (int i = 1; i < 31; i++) {
                MatchPost post1 = MatchPost.builder()
                        .writer(em.find(User.class, 1L))
                        .activity(Activity.WALK)
                        .topic(Topic.COMMON)
                        .location(Location.CAPITAL_EAST)
                        .budget(Budget.UNDER_5000)
                        .ageDifference(AgeDifference.WHATEVER)
                        .text("포스트 내용 " + i)
                        .startAt(LocalDateTime.of(2023, 03, i, (i * 6) % 20, 00))
                        .endAt(LocalDateTime.of(2023, 03, i, (i * 6) % 20 + 3, 00))
                        .isDeleted(false)
                        .build();

                MatchPost post2 = MatchPost.builder()
                        .writer(em.find(User.class, 1L))
                        .activity(Activity.COFFEE)
                        .topic(Topic.TRAVEL)
                        .location(Location.CAPITAL_WEST)
                        .budget(Budget.UNDER_10000)
                        .ageDifference(AgeDifference.WHATEVER)
                        .text("포스트 내용 " + i)
                        .startAt(LocalDateTime.of(2023, 03, i, (i * 6) % 20, 00))
                        .endAt(LocalDateTime.of(2023, 03, i, (i * 6) % 20 + 3, 00))
                        .isDeleted(false)
                        .build();

                MatchPost post3 = MatchPost.builder()
                        .writer(em.find(User.class, 1L))
                        .activity(Activity.MEAL)
                        .topic(Topic.SPORT)
                        .location(Location.CAPITAL_SOUTH)
                        .budget(Budget.UNDER_20000)
                        .ageDifference(AgeDifference.WHATEVER)
                        .text("포스트 내용 " + i)
                        .startAt(LocalDateTime.of(2023, 03, i, (i * 6) % 20, 00))
                        .endAt(LocalDateTime.of(2023, 03, i, (i * 6) % 20 + 3, 00))
                        .isDeleted(false)
                        .build();

                MatchPost post4 = MatchPost.builder()
                        .writer(em.find(User.class, 1L))
                        .activity(Activity.ALCOHOL)
                        .topic(Topic.MOVIE)
                        .location(Location.CAPITAL_NORTH)
                        .budget(Budget.UNDER_30000)
                        .ageDifference(AgeDifference.WHATEVER)
                        .text("포스트 내용 " + i)
                        .startAt(LocalDateTime.of(2023, 03, i, (i * 6) % 20, 00))
                        .endAt(LocalDateTime.of(2023, 03, i, (i * 6) % 20 + 3, 00))
                        .isDeleted(false)
                        .build();

                em.persist(post1);
                em.persist(post2);
                em.persist(post3);
                em.persist(post4);

                em.flush();
                em.clear();
            }
        }
    }
}
