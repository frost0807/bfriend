package com.frost.bfriend.repository.user;

import com.frost.bfriend.entity.QReview;
import com.frost.bfriend.entity.QUser;
import com.frost.bfriend.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.Optional;

import static com.frost.bfriend.entity.QReview.review;
import static com.frost.bfriend.entity.QUser.*;

public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public UserRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<Double> getAverageReviewScoreByUser(User currentUser) {
        Double averageReviewScore = queryFactory
                .select(review.score.avg())
                .from(user)
                .innerJoin(user.reviewsForMe, review)
                .where(user.eq(currentUser))
                .fetchOne();

        return Optional.ofNullable(averageReviewScore);
    }
}
