package com.frost.bfriend.repository.reply;

import com.frost.bfriend.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Long>, ReplyRepositoryCustom {
}
