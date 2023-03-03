package com.frost.bfriend.dto;

import com.frost.bfriend.common.constants.Sex;
import com.frost.bfriend.entity.MatchPost;
import com.frost.bfriend.entity.Reply;
import com.frost.bfriend.entity.User;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ReplyDto {

    @Getter
    public static class ReplyResponse {
        private Long replyId;

        private Long parentReplyId;

        private String username;

        private int age;

        private Sex sex;

        private String comment;

        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;

        private long minutesAfterCreate;

        private boolean isReplyOfMine;

        private boolean isReplySecret;

        private boolean isReplyDeleted;

        public ReplyResponse(Reply reply, User user) {
            this.replyId = reply.getId();
            this.parentReplyId = getParentReplyId(reply);
            this.username = reply.getUser().getName();
            this.sex = reply.getUser().getSex();
            this.age = calculateAge(reply.getUser().getBirthday());
            this.comment = handleReplyComment(reply, user);
            this.createdAt = reply.getCreatedAt();
            this.updatedAt = reply.getUpdatedAt();
            this.minutesAfterCreate = calculateMinutesAfterCreate(reply.getCreatedAt());
            this.isReplyOfMine = reply.getUser() == user;
            this.isReplySecret = isReplySecret(reply, user);
            this.isReplyDeleted = reply.getIsDeleted();
        }

        private Long getParentReplyId(Reply reply) {
            return reply.getParentReply() == null ?
                    null : reply.getParentReply().getId();
        }

        private String handleReplyComment(Reply reply, User user) {
            if (isReplySecret(reply, user) == true) {
                return "비밀 댓글입니다.";
            }
            if (reply.getIsDeleted() == true) {
                return "삭제된 댓글입니다.";
            }
            return reply.getComment();
        }

        private boolean isReplySecret(Reply reply, User user) {
            if (reply.getMatchPost().getWriter() == user) {
                return false;
            }
            if (reply.getParentReply() == null) {
                if (reply.getUser() == user) {
                    return false;
                }
                return true;
            }
            if (reply.getParentReply().getUser() == user) {
                return false;
            }
            return true;
        }

        private int calculateAge(LocalDate birthday) {
            return LocalDate.now().getYear() - birthday.getYear() + 1;
        }

        private long calculateMinutesAfterCreate(LocalDateTime createdAt) {
            return ChronoUnit.MINUTES.between(createdAt, LocalDateTime.now());
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class SaveReplyRequest {
        @NotNull
        private Long matchPostId;

        private Long parentReplyId;

        @NotBlank(message = "댓글을 입력해주세요")
        private String comment;

        public Reply toEntity(MatchPost matchPost, Reply parentReply, User user) {
            return Reply.builder()
                    .matchPost(matchPost)
                    .parentReply(parentReply)
                    .user(user)
                    .comment(this.comment)
                    .isDeleted(false)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateReplyRequest {
        @NotNull
        private Long replyId;

        @NotNull
        private Long matchPostId;

        private Long parentReplyId;

        @NotNull
        private Long writerId;

        @NotBlank(message = "댓글을 입력해주세요")
        private String comment;

        public Reply toEntity(MatchPost matchPost, Reply parentReply, User user) {
            return Reply.builder()
                    .id(this.replyId)
                    .matchPost(matchPost)
                    .parentReply(parentReply)
                    .user(user)
                    .comment(this.comment)
                    .build();
        }
    }
}