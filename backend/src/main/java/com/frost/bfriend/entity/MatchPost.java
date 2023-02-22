package com.frost.bfriend.entity;

import com.frost.bfriend.common.constants.*;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Entity
@Where(clause = "is_deleted='0'")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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
    private Topic topic;

    @Enumerated(EnumType.STRING)
    private Location location;

    @Enumerated(EnumType.STRING)
    private Budget budget;

    @Enumerated(EnumType.STRING)
    private AgeDifference ageDifference;

    private String text;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    private Boolean isDeleted;

    @OneToMany(mappedBy = "matchPost")
    private List<Reply> replies;

    public void delete() {
        this.isDeleted = true;
    }
}
