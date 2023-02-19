package com.frost.bfriend.dto;

import com.frost.bfriend.common.constants.*;
import com.frost.bfriend.entity.MatchPost;
import com.frost.bfriend.entity.User;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}