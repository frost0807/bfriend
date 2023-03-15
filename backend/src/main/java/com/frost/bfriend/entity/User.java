package com.frost.bfriend.entity;

import com.frost.bfriend.common.constants.Region;
import com.frost.bfriend.common.constants.Sex;
import com.frost.bfriend.common.constants.UserLevel;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends UserBase {

    private String name;

    private String phone;

    @Enumerated(EnumType.STRING)
    private Region region;

    @Enumerated(EnumType.STRING)
    private Sex sex;

    private LocalDate birthday;

    private Integer activityPoint;

    private Integer view;

    private Boolean isSuspended;

    private Boolean isDeleted;

    @OneToMany(mappedBy = "user")
    private List<QuestionAnswer> questionAnswers = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Review> reviewsForMe = new ArrayList<>();

    @OneToMany(mappedBy = "writer")
    private List<Review> reviewsByMe = new ArrayList<>();


    @Builder
    public User(Long id, String email, String password, UserLevel level, String name,
                String phone, Region region, Sex sex, LocalDate birthday,
                Integer activityPoint, Integer view, Boolean isSuspended, Boolean isDeleted) {
        super(id, email, password, level);
        this.name = name;
        this.phone = phone;
        this.region = region;
        this.sex = sex;
        this.birthday = birthday;
        this.activityPoint = activityPoint;
        this.view = view;
        this.isSuspended = isSuspended;
        this.isDeleted = isDeleted;
    }

    public void delete() {
        this.isDeleted = true;
    }

    public void increaseActivityPoint(int amount) {
        this.activityPoint += amount;
    }
}