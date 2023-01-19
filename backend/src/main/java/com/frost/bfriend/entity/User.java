package com.frost.bfriend.entity;

import com.frost.bfriend.constants.Region;
import com.frost.bfriend.constants.Sex;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends UserBase {

    private String name;

    private String phone;

    @Enumerated
    private Region region;

    @Enumerated(EnumType.STRING)
    private Sex sex;

    private LocalDateTime birthday;

    private Boolean isActivated;

    private Boolean isSuspended;
}
