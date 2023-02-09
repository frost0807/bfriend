package com.frost.bfriend.repository.reply;

import com.frost.bfriend.entity.MatchPost;
import com.frost.bfriend.entity.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReplyRepositoryCustom {
    Page<Reply> searchRepliesByMatchPost(Pageable pageable, MatchPost matchPost);
}
