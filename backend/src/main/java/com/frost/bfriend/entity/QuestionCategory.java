package com.frost.bfriend.entity;

import com.frost.bfriend.common.constants.QuestionCategoryName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
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

    @OneToMany(mappedBy = "questionCategory")
    private List<Question> questions = new ArrayList<>();
}
