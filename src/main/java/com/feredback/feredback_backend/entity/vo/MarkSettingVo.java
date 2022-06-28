package com.feredback.feredback_backend.entity.vo;

import com.feredback.feredback_backend.entity.MarkSetting;
import lombok.Data;

/**
 * @program: FE-Redback
 * @description:
 * @author: Xun Zhang (854776)
 * @date: 2022/5/8
 **/
@Data
public class MarkSettingVo {
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
     */
    private Double increment;

    private Integer templateId;

    private Integer rubricItemId;

    public MarkSettingVo() {
    }

    public MarkSettingVo(MarkSetting markSetting) {
        id = markSetting.getId();
        maximum = markSetting.getMaximum();
        weighting = markSetting.getWeighting();
        templateId = markSetting.getTemplateId();
        rubricItemId = markSetting.getRubricItemId();
        switch (markSetting.getIncrement()) {
            case 0:
                increment = 0.25;
                break;
            case 1:
                increment = 0.5;
                break;
            case 2:
                increment = 1.0;
                break;
        }
    }
}
