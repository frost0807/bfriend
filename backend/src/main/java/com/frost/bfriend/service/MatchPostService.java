package com.frost.bfriend.service;

import com.frost.bfriend.dto.MatchPostDto.Response;
import com.frost.bfriend.entity.MatchPost;
import com.frost.bfriend.entity.Reply;
import com.frost.bfriend.exception.matchpost.MatchPostNotFoundException;
import com.frost.bfriend.repository.matchpost.MatchPostRepository;
import com.frost.bfriend.repository.reply.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.frost.bfriend.dto.MatchPostDto.ListRequestCondition;
import static com.frost.bfriend.dto.MatchPostDto.ListResponse;
import static com.frost.bfriend.dto.ReplyDto.ReplyResponse;

@Service
@RequiredArgsConstructor
public class MatchPostService {

    private final MatchPostRepository matchPostRepository;

    private final ReplyRepository replyRepository;

    public Page<ListResponse> getMatchPostListAll(Pageable pageable, ListRequestCondition condition) {
        Page<ListResponse> listResponseNotCalculated =
                matchPostRepository.searchMatchPostsWithCondition(pageable, condition);
        long totalElements = listResponseNotCalculated.getTotalElements();
        List<ListResponse> listResponsesCalculated = listResponseNotCalculated.getContent().stream()
                .map(listResponse -> listResponse.calculateDayAndTimes())
                .collect(Collectors.toList());

        return new PageImpl<>(listResponsesCalculated, pageable, totalElements);
    }

    public Response getMatchPost(Long matchPostId) {
        MatchPost matchPost = matchPostRepository.searchMatchPostById(matchPostId)
                .orElseThrow(() -> new MatchPostNotFoundException("해당 게시물이 존재하지 않습니다."));
        return new Response(matchPost);
    }

//    /**
//     * 매칭글 작성자가 작성한 댓글
//     */
//    public Page<ReplyResponse> getRepliesByMatchPostId(Long userId, Pageable pageable, Long matchPostId) {
//        MatchPost matchPost = matchPostRepository.searchMatchPostById(matchPostId)
//                .orElseThrow(() -> new MatchPostNotFoundException("해당 게시물이 존재하지 않습니다."));
//        Page<Reply> replies = replyRepository.searchRepliesByMatchPost(pageable, matchPost);
//        List<ReplyResponse> replyResponsesFilteredByUser = replies.stream()
//                .map(reply -> reply.getUser().getId() == userId ? new ReplyResponse(reply) : null)
//                .collect(Collectors.toList());
//    }
}