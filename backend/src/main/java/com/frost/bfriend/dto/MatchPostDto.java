package com.frost.bfriend.dto;

import com.frost.bfriend.common.constants.*;
import com.frost.bfriend.entity.MatchPost;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
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
            this.timeFromTo = startAt.getHour() + " - " + endAt.getHour();

            return this;
        }
    }

    @Getter
    public static class Response {
        private Long matchPostId;

        private String name;

        private Activity activity;

        private Topic topic;

        private Location location;

        private Budget budget;

        private AgeDifference ageDifference;

        private String text;

        private LocalDateTime startAt;

        private LocalDateTime endAt;

        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;

        public Response(MatchPost matchPost) {
            this.matchPostId = matchPost.getId();
            this.name = matchPost.getWriter().getName();
            this.activity = matchPost.getActivity();
            this.topic = matchPost.getTopic();
            this.location = matchPost.getLocation();
            this.budget = matchPost.getBudget();
            this.ageDifference = matchPost.getAgeDifference();
            this.text = matchPost.getText();
            this.startAt = matchPost.getStartAt();
            this.endAt = matchPost.getEndAt();
            this.createdAt = matchPost.getCreatedAt();
            this.updatedAt = matchPost.getUpdatedAt();
        }
    }
}