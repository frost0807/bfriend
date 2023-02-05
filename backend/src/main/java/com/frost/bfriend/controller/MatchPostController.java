package com.frost.bfriend.controller;

import com.frost.bfriend.common.constants.Activity;
import com.frost.bfriend.common.constants.Duration;
import com.frost.bfriend.common.constants.Location;
import com.frost.bfriend.service.MatchPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.frost.bfriend.dto.MatchPostDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/matchposts")
public class MatchPostController {

    private final MatchPostService matchPostService;

    @GetMapping
    public ResponseEntity<Page<ListResponse>> getMatchPostListAll(
            @PageableDefault Pageable pageable,
            @RequestParam(required = false) Optional<Activity> activity,
            @RequestParam(required = false) Optional<Duration> duration,
            @RequestParam(required = false) Optional<Location> location) {
        ListRequestCondition condition = new ListRequestCondition(
                activity.orElseGet(() -> null),
                duration.orElseGet(() -> null),
                location.orElseGet(() -> null));

        return ResponseEntity.ok(matchPostService.getMatchPostListAll(pageable, condition));
    }
}