package com.frost.bfriend.dto;

import com.frost.bfriend.entity.Reply;
import lombok.Getter;

import java.time.LocalDateTime;

public class ReplyDto {

    @Getter
    public static class ReplyResponse {
        private Long replyId;

        private Long parentReplyId;

        private String name;

        private String comment;

        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;

        public ReplyResponse(Reply reply) {
            this.replyId = reply.getId();
            this.parentReplyId = getParentReplyId(reply);
            this.name = reply.getUser().getName();
            this.comment = reply.getComment();
            this.createdAt = reply.getCreatedAt();
            this.updatedAt = reply.getUpdatedAt();
        }

        private Long getParentReplyId(Reply reply) {
            if (reply.getParentReply() == null) {
                return null;
            }
            return reply.getParentReply().getId();
        }
    }
}
