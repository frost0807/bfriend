package com.frost.bfriend.repository.matchpost;

import com.frost.bfriend.entity.MatchPost;
import com.frost.bfriend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static com.frost.bfriend.dto.MatchPostDto.*;

public interface MatchPostRepositoryCustom {
    Page<ListResponse> searchMatchPostsWithCondition(Pageable pageable, MatchPostListCondition condition);

    Optional<MatchPost> searchMatchPostById(Long matchPostId);

    Page<MyMatchPostResponse> searchMatchPostsByUser(Pageable pageable, User user);
}
