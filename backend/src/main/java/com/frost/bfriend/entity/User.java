package com.frost.bfriend.entity;

import com.frost.bfriend.constants.Region;
import com.frost.bfriend.constants.Sex;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends UserBase {

    private String name;

    private String phone;

    @Enumerated(EnumType.STRING)
    private Region region;

    @Enumerated(EnumType.STRING)
    private Sex sex;

    private LocalDateTime birthday;

    private Boolean isActivated;

    private Boolean isSuspended;
}
