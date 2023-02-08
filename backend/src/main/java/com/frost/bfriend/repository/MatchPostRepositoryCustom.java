package com.frost.bfriend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static com.frost.bfriend.dto.MatchPostDto.ListRequestCondition;
import static com.frost.bfriend.dto.MatchPostDto.ListResponse;

public interface MatchPostRepositoryCustom {
    Page<ListResponse> searchMatchPostsWithCondition(Pageable pageable, ListRequestCondition condition);
}
