package com.frost.bfriend.controller;

import com.frost.bfriend.common.annotation.CheckUser;
import com.frost.bfriend.common.annotation.LoginUser;
import com.frost.bfriend.common.constants.Activity;
import com.frost.bfriend.common.constants.Location;
import com.frost.bfriend.common.constants.Topic;
import com.frost.bfriend.service.MatchPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.frost.bfriend.dto.MatchPostDto.*;
import static com.frost.bfriend.dto.ReplyDto.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/matchposts")
public class MatchPostController {

    private final MatchPostService matchPostService;

    @CheckUser
    @GetMapping
    public ResponseEntity<Page<ListResponse>> getMatchPostList(
            @PageableDefault Pageable pageable,
            @RequestParam(required = false) Optional<Activity> activity,
            @RequestParam(required = false) Optional<Topic> topic,
            @RequestParam(required = false) Optional<Location> location) {
        MatchPostListCondition condition = new MatchPostListCondition(
                activity.orElse(null),
                topic.orElse(null),
                location.orElse(null));

        return ResponseEntity.ok(matchPostService.getMatchPostList(pageable, condition));
    }

    @CheckUser
    @GetMapping("/{matchPostId}")
    public ResponseEntity<Response> getMatchPost(
            @LoginUser Long userId, @PathVariable Long matchPostId) {
        return ResponseEntity.ok(matchPostService.getMatchPost(userId, matchPostId));
    }

    @CheckUser
    @PostMapping
    public ResponseEntity<Long> saveMatchPost(
            @LoginUser Long userId, @RequestBody @Valid SaveRequest saveRequest) {
        return ResponseEntity.ok(matchPostService.saveMatchPost(userId, saveRequest));
    }

    @CheckUser
    @PutMapping
    public ResponseEntity<Void> updateMatchPost(
            @LoginUser Long userId, @RequestBody @Valid UpdateRequest updateRequest) {
        matchPostService.updateMatchPost(userId, updateRequest);

        return ResponseEntity.ok().build();
    }

    @CheckUser
    @DeleteMapping("/{matchPostId}")
    public ResponseEntity<Void> deletePost(
            @LoginUser Long userId, @PathVariable Long matchPostId) {
        matchPostService.deleteMatchPost(userId, matchPostId);
        return ResponseEntity.ok().build();
    }

    @CheckUser
    @GetMapping("/{matchPostId}/replies")
    public ResponseEntity<List<List<ReplyResponse>>> getRepliesByMatchPostId(
            @LoginUser Long userId, @PathVariable Long matchPostId) {

        return ResponseEntity.ok(matchPostService.getRepliesByMatchPostId(userId, matchPostId));
    }

    @CheckUser
    @PostMapping("/replies")
    public ResponseEntity<Void> saveReply(
            @LoginUser Long userId, @RequestBody SaveReplyRequest saveReplyRequest) {
        matchPostService.saveReply(userId, saveReplyRequest);

        return ResponseEntity.ok().build();
    }

    @CheckUser
    @PutMapping("/replies")
    public ResponseEntity<Void> updateReply(
            @LoginUser Long userId, @RequestBody UpdateReplyRequest updateReplyRequest) {
        matchPostService.updateReply(userId, updateReplyRequest);

        return ResponseEntity.ok().build();
    }

    @CheckUser
    @DeleteMapping("/replies/{replyId}")
    public ResponseEntity<Void> deleteReply(
            @LoginUser Long userId, @PathVariable Long replyId) {
        matchPostService.deleteReply(userId, replyId);

        return ResponseEntity.ok().build();
    }

    @CheckUser
    @GetMapping("/my")
    public ResponseEntity<Page<MyMatchPostResponse>> getMyMatchPostList(
            @PageableDefault Pageable pageable, @LoginUser Long userId) {
        return ResponseEntity.ok(matchPostService.getMyMatchPostList(pageable, userId));
    }

    @CheckUser
    @GetMapping("/replies/my")
    public ResponseEntity<Page<MyReplyResponse>> getMyReplyList(
            @PageableDefault Pageable pageable,
            @RequestParam(required = false) Optional<Activity> activity,
            @RequestParam(required = false) Optional<Location> location,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            Optional<LocalDate> fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            Optional<LocalDate> toDate,
            @LoginUser Long userId) {
        MatchPostConditionOfReplyList condition = new MatchPostConditionOfReplyList(
                activity.orElse(null),
                location.orElse(null),
                fromDate.orElse(null),
                toDate.orElse(null));

        return ResponseEntity.ok(matchPostService.getMyReplyList(pageable, condition, userId));
    }
}