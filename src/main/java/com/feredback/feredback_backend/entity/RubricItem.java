package com.feredback.feredback_backend.entity;

import lombok.Data;

/**
 * @program: FE-Redback
 * @description: entity class of rubric item (criteria), e.g voice
 * @author: Pinzhuo Zhao (1043915); Xun Zhang (854776)
 * @create: 2022-04-18 01:48
 **/
@Data
public class RubricItem extends BaseEntity{
    private Integer id;
    private String name;
    private Integer subjectId;
}
