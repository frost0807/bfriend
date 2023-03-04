package com.frost.bfriend.repository.reply;

import com.frost.bfriend.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReplyRepository extends JpaRepository<Reply, Long>, ReplyRepositoryCustom {
    Optional<Reply> findByIdAndIsDeletedFalse(Long replyId);
}
