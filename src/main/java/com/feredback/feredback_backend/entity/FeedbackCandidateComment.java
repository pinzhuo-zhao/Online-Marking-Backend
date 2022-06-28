package com.feredback.feredback_backend.entity;

import lombok.Data;

/**
 * @program: FE-Redback
 * @description: entity class of feedback candidate comment mid table
 * @author: Xun Zhang (854776)
 * @date: 2022/5/15
 **/
@Data
public class FeedbackCandidateComment extends BaseEntity{
    /**
     * id of the instance
     */
    private Integer id;

    /**
     * id of project candidate mid table
     */
    private Integer projectCandidateId;

    /**
     * id of rubric sub item
     */
    private Integer rubricSubItemId;

    /**
     * id of comment
     */
    private Integer commentId;
}
