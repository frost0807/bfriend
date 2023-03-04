package com.frost.bfriend.entity;

import com.frost.bfriend.common.constants.ReviewAnswer;
import com.frost.bfriend.common.constants.ReviewCategory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private User writer;

    @Enumerated(EnumType.STRING)
    private ReviewCategory category;

    @Enumerated(EnumType.STRING)
    private ReviewAnswer answer;

    private String comment;

    private Integer score;

    private Boolean isDeleted;
}
