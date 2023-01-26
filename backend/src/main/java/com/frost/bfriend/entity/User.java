package com.frost.bfriend.entity;

import com.frost.bfriend.constants.Region;
import com.frost.bfriend.constants.Sex;
import com.frost.bfriend.constants.UserLevel;
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

    private Boolean isActivated;

    private Boolean isSuspended;

    @Builder
    public User(Long id, String email, String password, UserLevel level, Boolean isDeleted,
                String name, String phone, Region region, Sex sex, LocalDate birthday,
                Boolean isActivated, Boolean isSuspended) {
        super(id, email, password, level, isDeleted);
        this.name = name;
        this.phone = phone;
        this.region = region;
        this.sex = sex;
        this.birthday = birthday;
        this.isActivated = isActivated;
        this.isSuspended = isSuspended;
    }
}