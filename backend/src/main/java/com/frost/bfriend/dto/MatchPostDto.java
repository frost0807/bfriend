package com.frost.bfriend.dto;

import com.frost.bfriend.common.constants.Activity;
import com.frost.bfriend.common.constants.Duration;
import com.frost.bfriend.common.constants.Location;
import com.frost.bfriend.entity.MatchPost;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
}