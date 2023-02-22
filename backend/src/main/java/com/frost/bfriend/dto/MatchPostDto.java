package com.frost.bfriend.dto;

import com.frost.bfriend.common.constants.*;
import com.frost.bfriend.entity.MatchPost;
import com.frost.bfriend.entity.User;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class MatchPostDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class ListRequestCondition {
        private Activity activity;

        private Topic topic;

        private Location location;
    }

    @Getter
    public static class ListResponse {
        private Long matchPostId;

        private Activity activity;

        private Topic topic;

        private Location location;

        private String text;

        private LocalDateTime startAt;

        private LocalDateTime endAt;

        private DayOfWeek dayOfTheWeek;

        private int daysLeft;

        private String timeFromTo;

        private int replyCount;

        @QueryProjection
        public ListResponse(Long matchPostId, Activity activity, Topic topic, Location location,
                            String text, LocalDateTime startAt, LocalDateTime endAt, int replyCount) {
            this.matchPostId = matchPostId;
            this.activity = activity;
            this.topic = topic;
            this.location = location;
            this.text = text;
            this.startAt = startAt;
            this.endAt = endAt;
            this.replyCount = replyCount;
        }

        public ListResponse calculateDayAndTimes() {
            this.dayOfTheWeek = startAt.getDayOfWeek();
            this.daysLeft = (int) ChronoUnit.DAYS.between(LocalDateTime.now(), startAt);
            this.timeFromTo = startAt.getHour() + "시 - " + endAt.getHour() + "시";

            return this;
        }
    }

    @Getter
    public static class Response {
        private Long matchPostId;

        private String username;

        private int age;

        private Sex sex;

        private Activity activity;

        private Topic topic;

        private Location location;

        private Budget budget;

        private AgeDifference ageDifference;

        private String text;

        private LocalDateTime startAt;

        private LocalDateTime endAt;

        private long minutesAfterCreate;

        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;

        private boolean isMatchPostOfMine;

        public Response(User user, MatchPost matchPost) {
            this.matchPostId = matchPost.getId();
            this.username = matchPost.getWriter().getName();
            this.sex = matchPost.getWriter().getSex();
            this.age = calculateAge(matchPost.getWriter().getBirthday());
            this.activity = matchPost.getActivity();
            this.topic = matchPost.getTopic();
            this.location = matchPost.getLocation();
            this.budget = matchPost.getBudget();
            this.ageDifference = matchPost.getAgeDifference();
            this.text = matchPost.getText();
            this.startAt = matchPost.getStartAt();
            this.endAt = matchPost.getEndAt();
            this.minutesAfterCreate = calculateMinutesAfterCreate(matchPost.getCreatedAt());
            this.createdAt = matchPost.getCreatedAt();
            this.updatedAt = matchPost.getUpdatedAt();
            this.isMatchPostOfMine = matchPost.getWriter() == user;
        }

        private int calculateAge(LocalDate birthday) {
            return LocalDate.now().getYear() - birthday.getYear() + 1;
        }

        private long calculateMinutesAfterCreate(LocalDateTime createdAt) {
            return ChronoUnit.MINUTES.between(createdAt, LocalDateTime.now());
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SaveRequest {

        @NotNull(message = "활동을 입력해주세요")
        private Activity activity;

        @NotNull(message = "주제를 입력해주세요")
        private Topic topic;

        @NotNull(message = "지역을 입력해주세요")
        private Location location;

        @NotNull(message = "예산을 입력해주세요")
        private Budget budget;

        @NotNull(message = "나이차 상관여부를 입력해주세요")
        private AgeDifference ageDifference;

        @NotBlank(message = "내용을 입력해주세요")
        private String text;

        @NotNull(message = "시작일시를 입력해주세요")
        private LocalDateTime startAt;

        @NotNull(message = "종료일시를 입력해주세요")
        private LocalDateTime endAt;

        public MatchPost toEntity(User user) {
            return MatchPost.builder()
                    .writer(user)
                    .activity(this.activity)
                    .topic(this.topic)
                    .location(this.location)
                    .budget(this.budget)
                    .ageDifference(this.ageDifference)
                    .text(this.text)
                    .startAt(this.startAt)
                    .endAt(this.endAt)
                    .isDeleted(false)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UpdateRequest {

        @NotNull(message = "게시물 아이디가 없습니다.")
        private Long matchPostId;

        @NotNull(message = "활동을 입력해주세요")
        private Activity activity;

        @NotNull(message = "주제를 입력해주세요")
        private Topic topic;

        @NotNull(message = "지역을 입력해주세요")
        private Location location;

        @NotNull(message = "예산을 입력해주세요")
        private Budget budget;

        @NotNull(message = "나이차 상관여부를 입력해주세요")
        private AgeDifference ageDifference;

        @NotBlank(message = "내용을 입력해주세요")
        private String text;

        @NotNull(message = "시작일시를 입력해주세요")
        private LocalDateTime startAt;

        @NotNull(message = "종료일시를 입력해주세요")
        private LocalDateTime endAt;

        public MatchPost toEntity(User user) {
            return MatchPost.builder()
                    .id(this.matchPostId)
                    .writer(user)
                    .activity(this.activity)
                    .topic(this.topic)
                    .location(this.location)
                    .budget(this.budget)
                    .ageDifference(this.ageDifference)
                    .text(this.text)
                    .startAt(this.startAt)
                    .endAt(this.endAt)
                    .isDeleted(false)
                    .build();
        }
    }
}