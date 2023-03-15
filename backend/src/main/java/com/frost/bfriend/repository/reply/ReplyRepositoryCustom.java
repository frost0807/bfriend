package com.frost.bfriend.repository.reply;

import com.frost.bfriend.dto.ReplyDto;
import com.frost.bfriend.entity.MatchPost;
import com.frost.bfriend.entity.Reply;
import com.frost.bfriend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.frost.bfriend.dto.ReplyDto.*;

public interface ReplyRepositoryCustom {
    List<Reply> searchRepliesByMatchPost(MatchPost matchPost);

    Page<MyReplyResponse> searchRepliesByUser(Pageable pageable, MatchPostConditionOfReplyList condition, User user);
}
