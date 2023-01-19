package com.frost.bfriend.entity;

import com.frost.bfriend.constants.UserLevel;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@DiscriminatorColumn
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class UserBase extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserLevel level;

    private Boolean isDeleted;
}
