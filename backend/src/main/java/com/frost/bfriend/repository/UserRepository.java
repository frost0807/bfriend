package com.frost.bfriend.repository;

import com.frost.bfriend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    boolean existsByEmailAndName(String email, String name);

    Optional<User> findByEmail(String email);
}
