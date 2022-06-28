package com.feredback.feredback_backend.entity;

import lombok.Data;

/**
 * @program: FE-Redback
 * @description: entity class of template, which is used to build the relationship between project and its rubric
 * @author: Pinzhuo Zhao (1043915); Xun Zhang (854776)
 * @create: 2022-04-10 00:53
 **/

@Data
public class Template extends BaseEntity {
    /**
     * id of the template instance
     */
    private Integer id;

    /**
     * name of the template instance
     */
    private String name;

    /**
     * description of the template instance
     */
    private String description;

    /**
     * id of the subject to which this template instance belongs to
     */
    private Integer subjectId;

}