package com.feredback.feredback_backend.entity;

import lombok.Data;

/**
 * @program: FE-Redback
 * @description: entity class of feedback mark setting
 * @author: Xun Zhang (854776)
 * @create: 2022/4/21
 **/
@Data
public class MarkSetting extends BaseEntity{
    /**
     * id of the mark setting instance
     */
    private Integer id;

    /**
     * maximum of the mark setting instance
     */
    private Integer maximum;

    /**
     * weighting of the mark setting instance
     */
    private Integer weighting;

    /**
     * mark increments of the mark setting instance
     * value: meaning
     * 0: 0.25
     * 1: 0.5
     * 2: 1
     */
    private Integer increment;

    private Integer templateId;

    private Integer rubricItemId;
}
