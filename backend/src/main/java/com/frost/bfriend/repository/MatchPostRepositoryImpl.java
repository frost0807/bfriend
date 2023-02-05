package com.frost.bfriend.repository;

import com.frost.bfriend.common.constants.Activity;
import com.frost.bfriend.common.constants.Duration;
import com.frost.bfriend.common.constants.Location;
import com.frost.bfriend.dto.QMatchPostDto_ListResponse;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.frost.bfriend.dto.MatchPostDto.ListRequestCondition;
import static com.frost.bfriend.dto.MatchPostDto.ListResponse;
import static com.frost.bfriend.entity.QMatchPost.matchPost;
import static com.frost.bfriend.entity.QReply.reply;

public class MatchPostRepositoryImpl implements MatchPostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MatchPostRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<ListResponse> searchMatchPostsWithCondition(
            Pageable pageable, ListRequestCondition condition) {
        List<ListResponse> matchPosts = queryFactory
                .select(new QMatchPostDto_ListResponse(
                        matchPost.id,
                        matchPost.activity,
                        matchPost.duration,
                        matchPost.location,
                        matchPost.comment,
                        matchPost.startAt,
                        reply.count().intValue()
                ))
                .from(matchPost)
                .leftJoin(matchPost.replies, reply)
                .where(
                        activityEq(condition.getActivity()),
                        durationEq(condition.getDuration()),
                        locationEq(condition.getLocation())
                )
                .orderBy(matchPost.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(matchPost.count())
                .from(matchPost)
                .where(
                        activityEq(condition.getActivity()),
                        durationEq(condition.getDuration()),
                        locationEq(condition.getLocation())
                );

        return PageableExecutionUtils.getPage(matchPosts, pageable, countQuery::fetchOne);
    }


    private BooleanExpression activityEq(Activity activity) {
        return activity != null ? matchPost.activity.eq(activity) : null;
    }

    private BooleanExpression durationEq(Duration duration) {
        return duration != null ? matchPost.duration.eq(duration) : null;
    }

    private BooleanExpression locationEq(Location location) {
        return location != null ? matchPost.location.eq(location) : null;
    }
}
