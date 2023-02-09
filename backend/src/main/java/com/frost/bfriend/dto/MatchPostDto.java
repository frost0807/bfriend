package com.frost.bfriend.dto;

import com.frost.bfriend.common.constants.*;
import com.frost.bfriend.entity.MatchPost;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class MatchPostDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class ListRequestCondition {
        private Activity activity;

        private Duration duration;

        private Location location;
    }

    @Getter
    public static class ListResponse {
        private Long matchPostId;

        private Activity activity;

        private Duration duration;

        private Location location;

        private String comment;

        private LocalDateTime startAt;

        private int replyCount;

        @QueryProjection
        public ListResponse(Long matchPostId, Activity activity, Duration duration,
                            Location location, String comment, LocalDateTime startAt,
                            int replyCount) {
            this.matchPostId = matchPostId;
            this.activity = activity;
            this.duration = duration;
            this.location = location;
            this.comment = comment;
            this.startAt = startAt;
            this.replyCount = replyCount;
        }
    }

    @Getter
    public static class Response {
        private Long matchPostId;

        private String name;

        private Activity activity;

        private Duration duration;

        private Location location;

        private Budget budget;

        private AgeDifference ageDifference;

        private String comment;

        private LocalDateTime startAt;

        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;

        public Response(MatchPost matchPost) {
            this.matchPostId = matchPost.getId();
            this.name = matchPost.getWriter().getName();
            this.activity = matchPost.getActivity();
            this.duration = matchPost.getDuration();
            this.location = matchPost.getLocation();
            this.budget = matchPost.getBudget();
            this.ageDifference = matchPost.getAgeDifference();
            this.comment = matchPost.getComment();
            this.startAt = matchPost.getStartAt();
            this.createdAt = matchPost.getCreatedAt();
            this.updatedAt = matchPost.getUpdatedAt();
        }
    }
}