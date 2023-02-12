package com.frost.bfriend.repository.matchpost;

import com.frost.bfriend.entity.MatchPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static com.frost.bfriend.dto.MatchPostDto.ListRequestCondition;
import static com.frost.bfriend.dto.MatchPostDto.ListResponse;

public interface MatchPostRepositoryCustom {
    Page<ListResponse> searchMatchPostsWithCondition(Pageable pageable, ListRequestCondition condition);
    Optional<MatchPost> searchMatchPostById(Long matchPostId);
}
