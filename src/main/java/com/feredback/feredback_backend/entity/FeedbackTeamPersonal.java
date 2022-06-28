package com.feredback.feredback_backend.entity;

import lombok.Data;

/**
 * @program: FE-Redback
 * @description: entity class of feedback candidate comment mid table
 * @author: Xun Zhang (854776)
 * @date: 2022/5/15
 **/
@Data
public class FeedbackTeamPersonal extends BaseEntity{
    /**
     * id of the instance
     */
    private Integer id;

    /**
     * id of project team mid table
     */
    private Integer projectTeamId;

    /**
     * id of candidate
     */
    private Integer candidateId;

    /**
     * feedback
     */
    private String feedback;
}
