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
import java.time.LocalDate;

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

    private Boolean isSuspended;

    private Boolean isDeleted;


    @Builder
    public User(Long id, String email, String password, UserLevel level, String name,
                String phone, Region region, Sex sex, LocalDate birthday,
                Integer activityPoint, Boolean isSuspended, Boolean isDeleted) {
        super(id, email, password, level);
        this.name = name;
        this.phone = phone;
        this.region = region;
        this.sex = sex;
        this.birthday = birthday;
        this.activityPoint = activityPoint;
        this.isSuspended = isSuspended;
        this.isDeleted = isDeleted;
    }
}