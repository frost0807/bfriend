package com.frost.bfriend.service;

import com.frost.bfriend.dto.ReplyDto;
import com.frost.bfriend.entity.MatchPost;
import com.frost.bfriend.entity.Reply;
import com.frost.bfriend.entity.User;
import com.frost.bfriend.exception.matchpost.ForbiddenMatchPostException;
import com.frost.bfriend.exception.matchpost.ForbiddenReplyException;
import com.frost.bfriend.exception.matchpost.MatchPostNotFoundException;
import com.frost.bfriend.exception.matchpost.ReplyNotFoundException;
import com.frost.bfriend.exception.user.UserNotFoundException;
import com.frost.bfriend.repository.matchpost.MatchPostRepository;
import com.frost.bfriend.repository.reply.ReplyRepository;
import com.frost.bfriend.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.frost.bfriend.dto.MatchPostDto.*;
import static com.frost.bfriend.dto.ReplyDto.ReplyResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchPostService {

    private final UserRepository userRepository;
    private final MatchPostRepository matchPostRepository;
    private final ReplyRepository replyRepository;

    @Transactional(readOnly = true)
    public Page<ListResponse> getMatchPostListAll(Pageable pageable, ListRequestCondition condition) {
        Page<ListResponse> listResponseNotCalculated =
                matchPostRepository.searchMatchPostsWithCondition(pageable, condition);
        long totalElements = listResponseNotCalculated.getTotalElements();
        List<ListResponse> listResponsesCalculated = listResponseNotCalculated.getContent().stream()
                .map(listResponse -> listResponse.calculateDayAndTimes())
                .collect(Collectors.toList());

        return new PageImpl<>(listResponsesCalculated, pageable, totalElements);
    }

    @Transactional(readOnly = true)
    public Response getMatchPost(Long userId, Long matchPostId) {
        MatchPost matchPost = matchPostRepository.searchMatchPostById(matchPostId)
                .orElseThrow(() -> new MatchPostNotFoundException("해당 게시물이 존재하지 않습니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("해당 유저가 존재하지 않습니다."));

        return new Response(user, matchPost);
    }

    @Transactional
    public Long saveMatchPost(Long userId, SaveRequest requestDto) {
        User user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 유저입니다."));
        MatchPost matchPost = requestDto.toEntity(user);
        matchPostRepository.save(matchPost);
        user.increaseActivityPoint(10);
        userRepository.save(user);

        return matchPost.getId();
    }

    @Transactional
    public void updateMatchPost(Long userId, UpdateRequest requestDto) {
        User user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 유저입니다."));
        MatchPost matchPost = matchPostRepository.findById(requestDto.getMatchPostId())
                .orElseThrow(() -> new MatchPostNotFoundException("해당 게시물이 존재하지 않습니다."));

        if (matchPost.getWriter() != user) {
            throw new ForbiddenMatchPostException("본인의 게시물이 아닙니다.");
        }
        matchPostRepository.save(requestDto.toEntity(user));
    }

    @Transactional
    public void deleteMatchPost(Long userId, Long matchPostId) {
        User user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 유저입니다."));
        MatchPost matchPost = matchPostRepository.findById(matchPostId)
                .orElseThrow(() -> new MatchPostNotFoundException("해당 게시물이 존재하지 않습니다."));

        if (matchPost.getWriter() != user) {
            throw new ForbiddenMatchPostException("본인의 게시물이 아닙니다.");
        }
        matchPost.delete();
        matchPostRepository.save(matchPost);
    }

    /**
     * 로그인한 유저가 매칭글 작성자 -> 모든 댓글을 다 조회가능
     * 로그인한 유저가 매칭글 작성자가 X -> 본인의 댓글 그룹만 조회가능
     * <p>
     * 댓글 정렬은 내림차순
     * 대댓글 정렬은 오름차순
     * <p>
     * 비밀댓글은 내용에 "비밀 댓글입니다." 출력
     * 삭제된 댓글은 내용에 "삭제된 댓글입니다."출력
     */
    @Transactional(readOnly = true)
    public List<List<ReplyResponse>> getRepliesByMatchPostId(Long userId, Long matchPostId) {
        MatchPost matchPost = matchPostRepository.searchMatchPostById(matchPostId)
                .orElseThrow(() -> new MatchPostNotFoundException("해당 게시물이 존재하지 않습니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("해당 유저가 존재하지 않습니다."));
        List<Reply> replies = replyRepository.searchRepliesByMatchPost(matchPost);

        Map<Long, List<ReplyResponse>> replyGroupResponseMap = replies.stream()
                .map(reply -> new ReplyResponse(reply, user))
                .collect(Collectors.groupingBy(
                        replyResponse -> replyResponse.getParentReplyId() == null ?
                                replyResponse.getReplyId() : replyResponse.getParentReplyId(),
                        LinkedHashMap::new, Collectors.toCollection(ArrayList::new)));

        List<List<ReplyResponse>> replyGroupResponses = new ArrayList<>(replyGroupResponseMap.values());
        Collections.reverse(replyGroupResponses);

        return replyGroupResponses;
    }

    /**
     * 매칭글 작성자는 본인의 글에 댓글 작성 불가
     * <p>
     * 대댓글은 매칭글 작성자 or 댓글을 작성한 당사자만 작성 가능
     * <p>
     * 댓글이 삭제되어도 해당 댓글에 대댓글은 작성 가능
     */
    @Transactional
    public void saveReply(Long userId, ReplyDto.SaveReplyRequest saveReplyRequest) {
        User user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new UserNotFoundException("해당 유저가 존재하지 않습니다."));
        MatchPost matchPost = matchPostRepository.findById(saveReplyRequest.getMatchPostId())
                .orElseThrow(() -> new MatchPostNotFoundException("해당 게시물이 존재하지 않습니다."));
        Reply parentReply = null;

        if (saveReplyRequest.getParentReplyId() != null) {
            parentReply = replyRepository.findById(saveReplyRequest.getParentReplyId())
                    .orElseThrow(() -> new ReplyNotFoundException("상위 댓글이 존재하지 않습니다."));

            if (!(matchPost.getWriter().equals(user) || parentReply.getUser().equals(user))) {
                throw new ForbiddenReplyException("댓글을 작성할 권한이 없습니다.");
            }
        } else if (matchPost.getWriter().equals(user)) {
            throw new ForbiddenReplyException("매칭글 작성자는 대댓글만 작성할 수 있습니다.");
        }

        Reply reply = saveReplyRequest.toEntity(matchPost, parentReply, user);
        replyRepository.save(reply);
        user.increaseActivityPoint(5);
        userRepository.save(user);
    }

    @Transactional
    public void updateReply(Long userId, ReplyDto.UpdateReplyRequest updateReplyRequest) {
        User user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new UserNotFoundException("해당 유저가 존재하지 않습니다."));
        MatchPost matchPost = matchPostRepository.findById(updateReplyRequest.getMatchPostId())
                .orElseThrow(() -> new MatchPostNotFoundException("해당 게시물이 존재하지 않습니다."));
        Reply reply = replyRepository.findByIdAndIsDeletedFalse(updateReplyRequest.getReplyId())
                .orElseThrow(() -> new ReplyNotFoundException("해당 댓글이 존재하지 않습니다."));
        Reply parentReply = null;

        if (!reply.getUser().equals(user)) {
            throw new ForbiddenReplyException("본인의 댓글 혹은 대댓글이 아닙니다.");
        }
        if (updateReplyRequest.getParentReplyId() != null) {
            parentReply = replyRepository.findById(updateReplyRequest.getParentReplyId())
                    .orElseThrow(() -> new ReplyNotFoundException("상위 댓글이 존재하지 않습니다."));
        }
        Reply updatedReply = updateReplyRequest.toEntity(matchPost, parentReply, user);
        replyRepository.save(updatedReply);
    }

    @Transactional
    public void deleteReply(Long userId, Long replyId) {
        User user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new UserNotFoundException("해당 유저가 존재하지 않습니다."));
        Reply reply = replyRepository.findByIdAndIsDeletedFalse(replyId)
                .orElseThrow(() -> new ReplyNotFoundException("해당 댓글이 존재하지 않습니다."));
        if (!reply.getUser().equals(user)) {
            throw new ForbiddenReplyException("본인의 댓글 혹은 대댓글이 아닙니다.");
        }
        reply.delete();
        replyRepository.save(reply);
    }
}