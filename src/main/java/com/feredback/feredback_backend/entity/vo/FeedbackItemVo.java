package com.feredback.feredback_backend.entity.vo;

import lombok.Data;

import java.util.List;

/**
 * @program: FE-Redback
 * @description: View class of feedback item sent to front-end
 * @author: Xun Zhang (854776)
 * @date: 2022/5/14
 **/
@Data
public class FeedbackItemVo {
    /**
     * id of rubric item
     */
    private Integer id;

    /**
     * name of rubric item
     */
    private String name;

    /**
     * mark for this rubric item
     */
    private Double mark;

    /**
     * maximum of this rubric item
     */
    private Integer maximum;

    /**
     * weighting of this rubric item
     */
    private Integer weighting;

    /**
     * rubric sub item of this item
     */
    private List<FeedbackSubItemVo> subItems;

    private String additionalComment;
}
