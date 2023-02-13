package com.frost.bfriend.controller;

import com.frost.bfriend.common.annotation.CheckUser;
import com.frost.bfriend.common.annotation.LoginUser;
import com.frost.bfriend.common.constants.Activity;
import com.frost.bfriend.common.constants.Location;
import com.frost.bfriend.common.constants.Topic;
import com.frost.bfriend.dto.ReplyDto;
import com.frost.bfriend.service.MatchPostService;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Check;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.frost.bfriend.dto.MatchPostDto.*;
import static com.frost.bfriend.dto.ReplyDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/matchposts")
public class MatchPostController {

    private final MatchPostService matchPostService;

    @CheckUser
    @GetMapping
    public ResponseEntity<Page<ListResponse>> getMatchPostListAll(
            @PageableDefault Pageable pageable,
            @RequestParam(required = false) Optional<Activity> activity,
            @RequestParam(required = false) Optional<Topic> topic,
            @RequestParam(required = false) Optional<Location> location) {
        ListRequestCondition condition = new ListRequestCondition(
                activity.orElseGet(() -> null),
                topic.orElseGet(() -> null),
                location.orElseGet(() -> null));

        return ResponseEntity.ok(matchPostService.getMatchPostListAll(pageable, condition));
    }

    @CheckUser
    @GetMapping("/{matchPostId}")
    public ResponseEntity<Response> getMatchPost(@PathVariable Long matchPostId) {
        return ResponseEntity.ok(matchPostService.getMatchPost(matchPostId));
    }

//    @CheckUser
//    @GetMapping("/{matchPostId}/replies")
//    public ResponseEntity<Page<ReplyResponse>> getRepliesByMatchPostId(
//            @PageableDefault Pageable pageable, @LoginUser Long userId, @PathVariable Long matchPostId) {
//        return ResponseEntity.ok(matchPostService.getRepliesByMatchPostId(userId, pageable, matchPostId));
//    }
}