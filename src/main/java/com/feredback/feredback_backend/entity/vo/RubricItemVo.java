package com.feredback.feredback_backend.entity.vo;

import com.feredback.feredback_backend.entity.RubricItem;
import lombok.Data;

import java.util.List;

/**
 * @program: FE-Redback
 * @description: VO class of rubric item, describe objects sent to front-end
 * @author: Pinzhuo Zhao (1043915); Xun Zhang (854776)
 * @create: 2022-04-19 20:12
 **/
@Data
public class RubricItemVo {
    /**
     * the rubric item
     */
    private RubricItem rubricItem;

    /**
     * a list of sub-items which belongs to the rubric item
     */
    private List<RubricSubItemVo> rubricSubItems;

    /**
     * mark setting
     */
    private MarkSettingVo markSetting;
}
