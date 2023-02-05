package com.frost.bfriend.entity;

import com.frost.bfriend.common.constants.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@Where(clause = "is_deleted='0'")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchPost extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private User writer;

    @Enumerated(EnumType.STRING)
    private Activity activity;

    @Enumerated(EnumType.STRING)
    private Duration duration;

    @Enumerated(EnumType.STRING)
    private Location location;

    @Enumerated(EnumType.STRING)
    private Budget budget;

    @Enumerated(EnumType.STRING)
    private AgeDifference ageDifference;

    private String comment;

    private LocalDateTime startAt;

    private Boolean isDeleted;

    @OneToMany(mappedBy = "matchPost")
    private List<Reply> replies;
}
