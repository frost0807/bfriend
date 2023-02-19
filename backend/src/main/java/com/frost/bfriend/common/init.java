//package com.frost.bfriend.common;
//
//import com.frost.bfriend.common.constants.*;
//import com.frost.bfriend.common.util.encryption.EncryptionService;
//import com.frost.bfriend.entity.*;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Profile;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.annotation.PostConstruct;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Random;
//
//import static com.frost.bfriend.dto.UserDto.SaveRequest;
//
//@Slf4j
//@Profile("local")
//@Component
//@RequiredArgsConstructor
//public class init {
//
//    private final InitService initService;
//
//
//    @PostConstruct
//    @Transactional
//    public void init() {
//        initService.initUser();
//        initService.initQuestionAnswer();
//        initService.initMatchPost();
//        initService.initReply();
//    }
//
//    @RequiredArgsConstructor
//    @Component
//    static class InitService {
//        @PersistenceContext
//        private EntityManager em;
//        private final EncryptionService encryptionService;
//
//        @Transactional
//        public void initUser() {
//            for (int i = 1; i < 6; i++) {
//                SaveRequest userDto = SaveRequest.builder()
//                        .email("guest" + i + "@bfriend.com")
//                        .phone("0100000000" + i)
//                        .password("1234qwer!")
//                        .name("유저" + i)
//                        .sex(Sex.MALE)
//                        .region(Region.SEOUL)
//                        .birthday(LocalDate.now())
//                        .build();
//                userDto.encryptPassword(encryptionService);
//                em.persist(userDto.toEntity());
//            }
//            em.flush();
//            em.clear();
//        }
//
//        @Transactional
//        public void initQuestionAnswer() {
//            QuestionCategory personality = QuestionCategory.builder().name(QuestionCategoryName.PERSONALITY).build();
//            QuestionCategory like = QuestionCategory.builder().name(QuestionCategoryName.LIKE).build();
//            QuestionCategory dislike = QuestionCategory.builder().name(QuestionCategoryName.DISLIKE).build();
//            QuestionCategory hobby = QuestionCategory.builder().name(QuestionCategoryName.HOBBY).build();
//            QuestionCategory etc = QuestionCategory.builder().name(QuestionCategoryName.ETC).build();
//            List<QuestionCategory> questionCategories = Arrays.asList(personality, like, dislike, hobby, etc);
//            em.persist(personality);
//            em.persist(like);
//            em.persist(dislike);
//            em.persist(hobby);
//            em.persist(etc);
//
//            em.flush();
//            em.clear();
//
//            for (int i = 0; i < 5; i++) {
//                for (int j = 1; j < 6; j++) {
//                    Question question = Question.builder()
//                            .questionCategory(questionCategories.get(i))
//                            .content(questionCategories.get(i).getName().getName() + "질문 " + j)
//                            .build();
//                    em.persist(question);
//                }
//            }
//            em.flush();
//            em.clear();
//
//            log.info(em.find(User.class, 1L).getName());
//            log.info(em.find(Question.class, 1).getContent());
//
//            for (int i = 1; i < 25; i += 2) {
//                log.info("i = {}", i);
//                QuestionAnswer questionAnswer = QuestionAnswer.builder()
//                        .user(em.find(User.class, 1L))
//                        .question(em.find(Question.class, i))
//                        .answer("답변 " + i)
//                        .build();
//                em.persist(questionAnswer);
//            }
//        }
//
//        @Transactional
//        public void initMatchPost() {
//            Random random = new Random();
//            List<Activity> activities = Arrays.asList(Activity.values());
//            List<Topic> topics = Arrays.asList(Topic.values());
//            List<Location> locations = Arrays.asList(Location.values());
//            List<Budget> budgets = Arrays.asList(Budget.values());
//            List<AgeDifference> ageDifferences = Arrays.asList(AgeDifference.values());
//
//
//            for (int i = 1; i < 500; i++) {
//                MatchPost post = MatchPost.builder()
//                        .writer(em.find(User.class, random.nextInt(5) + 1L))
//                        .activity(activities.get(random.nextInt(5)))
//                        .topic(topics.get(random.nextInt(9)))
//                        .location(locations.get(random.nextInt(4)))
//                        .budget(budgets.get(random.nextInt(7)))
//                        .ageDifference(ageDifferences.get(random.nextInt(2)))
//                        .text("포스트 내용 " + i)
//                        .startAt(LocalDateTime.of(2023, 03, (i % 30) + 1, (i * 6) % 20, 00))
//                        .endAt(LocalDateTime.of(2023, 03, (i % 31) + 1, (i * 6) % 20 + 3, 00))
//                        .isDeleted(false)
//                        .build();
//
//                em.persist(post);
//            }
//            em.flush();
//            em.clear();
//        }
//
//        @Transactional
//        public void initReply() {
//            Random random = new Random();
//            List<Activity> activities = Arrays.asList(Activity.values());
//            List<Topic> topics = Arrays.asList(Topic.values());
//            List<Location> locations = Arrays.asList(Location.values());
//            List<Budget> budgets = Arrays.asList(Budget.values());
//            List<AgeDifference> ageDifferences = Arrays.asList(AgeDifference.values());
//
//            MatchPost post = MatchPost.builder()
//                    .writer(em.find(User.class, 1L))
//                    .activity(activities.get(random.nextInt(5)))
//                    .topic(topics.get(random.nextInt(9)))
//                    .location(locations.get(random.nextInt(4)))
//                    .budget(budgets.get(random.nextInt(7)))
//                    .ageDifference(ageDifferences.get(random.nextInt(2)))
//                    .text("댓글 테스트용 포스트")
//                    .startAt(LocalDateTime.now())
//                    .endAt(LocalDateTime.now())
//                    .isDeleted(false)
//                    .build();
//
//            em.persist(post);
//            em.flush();
//            em.clear();
//
//            for (long i = 1; i < 5; i++) {
//                Reply parent = Reply.builder()
//                        .user(em.find(User.class, i + 1))
//                        .parentReply(null)
//                        .matchPost(em.find(MatchPost.class, 500L))
//                        .comment("부모 댓글입니다." + i)
//                        .isDeleted(false)
//                        .build();
//                em.persist(parent);
//            }
//            em.flush();
//            em.clear();
//            for (long i = 1; i < 5; i++) {
//                for (long j = 0; j < 5; j++) {
//                    Reply child1 = Reply.builder()
//                            .user(em.find(User.class, 1L))
//                            .parentReply(em.find(Reply.class, i))
//                            .matchPost(em.find(MatchPost.class, 500L))
//                            .comment("자식 댓글입니다." + j)
//                            .isDeleted(false)
//                            .build();
//
//                    Reply child2 = Reply.builder()
//                            .user(em.find(User.class, i + 1))
//                            .parentReply(em.find(Reply.class, i))
//                            .matchPost(em.find(MatchPost.class, 500L))
//                            .comment("자식 댓글입니다." + j)
//                            .isDeleted(false)
//                            .build();
//                    em.persist(child1);
//                    em.persist(child2);
//                    em.flush();
//                    em.clear();
//                }
//            }
//        }
//    }
//}
