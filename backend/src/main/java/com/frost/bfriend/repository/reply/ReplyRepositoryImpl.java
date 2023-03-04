package com.frost.bfriend.repository.reply;

import com.frost.bfriend.entity.MatchPost;
import com.frost.bfriend.entity.Reply;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

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
}
