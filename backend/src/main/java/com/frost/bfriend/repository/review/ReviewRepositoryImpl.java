package com.frost.bfriend.repository.review;

import com.frost.bfriend.entity.QReview;
import com.frost.bfriend.entity.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.Optional;

import static com.frost.bfriend.entity.QReview.*;

public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ReviewRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

}
