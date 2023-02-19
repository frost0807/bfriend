package com.frost.bfriend.repository.reply;

import com.frost.bfriend.entity.MatchPost;
import com.frost.bfriend.entity.QMatchPost;
import com.frost.bfriend.entity.QReply;
import com.frost.bfriend.entity.Reply;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Coalesce;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

import static com.frost.bfriend.entity.QMatchPost.*;
import static com.frost.bfriend.entity.QReply.reply;
import static com.frost.bfriend.entity.QUser.user;

public class ReplyRepositoryImpl implements ReplyRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ReplyRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<Reply> searchRepliesByMatchPost(MatchPost matchPost) {
        return queryFactory
                .selectFrom(reply)
                .distinct()
                .join(reply.matchPost, QMatchPost.matchPost)
                .join(reply.user, user)
                .fetchJoin()
                .orderBy(reply.id.asc())
                .fetch();
    }
}
