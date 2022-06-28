package com.feredback.feredback_backend.entity.vo;

import lombok.Data;

/**
 * @program: FE-Redback
 * @description:
 * @author: Xun Zhang (854776)
 * @date: 2022/5/17
 **/
@Data
public class FeedbackSubItemVo {
    /**
     * id of rubric sub item
     */
    private Integer id;

    /**
     * name of rubric sub item
     */
    private String name;

    /**
     * id of comment
     */
    private Integer commentId;

    /**
     * comment for this rubric sub item
     */
    private String comment;
}
