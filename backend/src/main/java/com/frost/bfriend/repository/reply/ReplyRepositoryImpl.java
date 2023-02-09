package com.frost.bfriend.repository.reply;

import com.frost.bfriend.entity.MatchPost;
import com.frost.bfriend.entity.Reply;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.frost.bfriend.entity.QReply.reply;
import static com.frost.bfriend.entity.QUser.user;

public class ReplyRepositoryImpl implements ReplyRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ReplyRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public Page<Reply> searchRepliesByMatchPost(Pageable pageable, MatchPost matchPost) {
        List<Reply> replies = queryFactory
                .selectFrom(reply)
                .join(reply.user, user)
                .fetchJoin()
                .where(reply.matchPost.eq(matchPost))
                .orderBy(reply.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(reply.count())
                .from(reply)
                .where(reply.matchPost.eq(matchPost));

        return PageableExecutionUtils.getPage(replies, pageable, countQuery::fetchOne);
    }
}
