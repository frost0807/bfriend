package com.frost.bfriend.repository;

import com.frost.bfriend.entity.MatchPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchPostRepository extends JpaRepository<MatchPost, Long>, MatchPostRepositoryCustom {
}
