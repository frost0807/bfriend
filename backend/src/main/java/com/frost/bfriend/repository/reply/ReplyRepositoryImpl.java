package com.frost.bfriend.repository.reply;

import com.frost.bfriend.common.constants.Activity;
import com.frost.bfriend.common.constants.Location;
import com.frost.bfriend.common.constants.Topic;
import com.frost.bfriend.dto.QReplyDto_MyReplyResponse;
import com.frost.bfriend.entity.MatchPost;
import com.frost.bfriend.entity.Reply;
import com.frost.bfriend.entity.User;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

import static com.frost.bfriend.dto.ReplyDto.MatchPostConditionOfReplyList;
import static com.frost.bfriend.dto.ReplyDto.MyReplyResponse;
import static com.frost.bfriend.entity.QMatchPost.matchPost;
import static com.frost.bfriend.entity.QReply.reply;
import static com.frost.bfriend.entity.QUser.user;

public class ReplyRepositoryImpl implements ReplyRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ReplyRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Reply> searchRepliesByMatchPost(MatchPost matchPost) {
        return queryFactory
                .selectFrom(reply)
                .distinct()
                .join(reply.user, user)
                .fetchJoin()
                .where(reply.matchPost.eq(matchPost))
                .orderBy(reply.id.asc())
                .fetch();
    }

    @Override
    public Page<MyReplyResponse> searchRepliesByUser(
            Pageable pageable, MatchPostConditionOfReplyList condition, User user) {
        List<MyReplyResponse> replies = queryFactory
                .select(new QReplyDto_MyReplyResponse(
                        reply.matchPost.id,
                        reply.matchPost.activity,
                        reply.matchPost.topic,
                        reply.matchPost.location,
                        reply.matchPost.text,
                        reply.matchPost.startAt,
                        reply.matchPost.endAt,
                        reply.matchPost.replies.size(),
                        reply.id,
                        reply.comment,
                        reply.createdAt
                ))
                .from(reply)
                .innerJoin(reply.matchPost, matchPost)
                .where(reply.user.eq(user),
                        activityEq(condition.getActivity()),
                        locationEq(condition.getLocation()),
                        dateBetween(condition.getFromDate(), condition.getToDate()))
                .orderBy(reply.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(reply.count())
                .from(reply)
                .where(reply.user.eq(user));

        return PageableExecutionUtils.getPage(replies, pageable, countQuery::fetchOne);
    }

    private BooleanExpression activityEq(Activity activity) {
        return activity != null ? matchPost.activity.eq(activity) : null;
    }

    private BooleanExpression locationEq(Location location) {
        return location != null ? matchPost.location.eq(location) : null;
    }

    private BooleanExpression dateBetween(LocalDate fromDate, LocalDate toDate) {
        return fromDate != null && toDate != null ? reply.matchPost.startAt.between(
                fromDate.atStartOfDay(), toDate.plusDays(1).atStartOfDay()) : null;
    }
}
