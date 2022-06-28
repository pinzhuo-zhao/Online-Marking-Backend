package com.feredback.feredback_backend.entity;

import lombok.Data;

/**
 * @program: FE-Redback
 * @description: entity class of feedback candidate comment mid table
 * @author: Xun Zhang (854776)
 * @date: 2022/5/15
 **/
@Data
public class FeedbackCandidateMark extends BaseEntity{
    /**
     * id of the instance
     */
    private Integer id;

    /**
     * id of project candidate mid table
     */
    private Integer projectCandidateId;

    /**
     * id of rubric item
     */
    private Integer rubricItemId;

    /**
     * mark
     */
    private Double mark;

    /**
     * additional Comment (optional)
     */
    private String additionalComment;
}
