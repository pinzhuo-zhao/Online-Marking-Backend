package com.feredback.feredback_backend.entity;

import lombok.Data;

/**
 * @program: FE-Redback
 * @description: entity class of comment
 * @author: Xun Zhang (854776)
 * @create: 2022/4/21
 **/
@Data
public class Comment extends BaseEntity{
    /**
     * id of comment instance
     */
    private Integer id;

    /**
     * content of comment instance
     */
    private String content;

    /**
     * level of comment instance
     * value: meaning
     * -1 - negative
     * 0 - neutral
     * 1 - positive
     */
    private Integer level;
}
