package com.frost.bfriend.repository.user;

import com.frost.bfriend.entity.User;

import java.util.Optional;

public interface UserRepositoryCustom {
    Optional<Double> getAverageReviewScoreByUser(User user);
}
