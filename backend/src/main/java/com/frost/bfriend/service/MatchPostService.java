package com.frost.bfriend.service;

import com.frost.bfriend.repository.MatchPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.frost.bfriend.dto.MatchPostDto.ListRequestCondition;
import static com.frost.bfriend.dto.MatchPostDto.ListResponse;

@Service
@RequiredArgsConstructor
public class MatchPostService {

    private final MatchPostRepository matchPostRepository;

    public Page<ListResponse> getMatchPostListAll(Pageable pageable, ListRequestCondition condition) {
        return matchPostRepository.searchMatchPostsWithCondition(pageable, condition);
    }
}
