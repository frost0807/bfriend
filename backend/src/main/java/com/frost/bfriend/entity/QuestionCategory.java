package com.frost.bfriend.entity;

import com.frost.bfriend.common.constants.QuestionCategoryName;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Builder
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class QuestionCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private QuestionCategoryName name;

//    @OneToMany(mappedBy = "question_category")
//    private List<Question> questions;
}
