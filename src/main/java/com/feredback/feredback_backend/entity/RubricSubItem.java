package com.feredback.feredback_backend.entity;

import lombok.Data;

/**
 * @program: FE-Redback
 * @description: entity class of rubric sub-item, e.g. eye contact of body language
 * @author: Pinzhuo Zhao (1043915); Xun Zhang (854776)
 * @create: 2022-04-19 20:13
 **/
@Data
public class RubricSubItem extends BaseEntity {
    /**
     * id of rubric sub-item instance
     */
    private Integer id;

    /**
     * name of rubric sub-item instance
     */
    private String name;

    /**
     * id of the rubric item to which this sub-item instance belongs to
     */
    private Integer rubricItemId;

}
