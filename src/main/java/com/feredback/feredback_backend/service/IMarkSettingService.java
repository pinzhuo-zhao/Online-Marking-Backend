package com.feredback.feredback_backend.service;

import com.feredback.feredback_backend.entity.MarkSetting;
import com.feredback.feredback_backend.entity.vo.MarkSettingVo;

import java.util.List;

/**
 * @program: FE-Redback
 * @description: Service interface of mark setting
 * @author: Xun Zhang (854776)
 * @date: 2022/5/8
 **/
public interface IMarkSettingService {
    /**
     * insert mark setting
     *
     * @param entity given mark setting
     */
    void createMarkSetting(MarkSetting entity);

    /**
     * get mark setting by its id
     *
     * @param id given mark setting id
     * @return mark setting with given id; or null if not find
     */
    MarkSettingVo getById(Integer id);

    /**
     * search mark settings by searching condition
     *
     * @param condition searching condition of mark setting
     * @return List of mark settings with given searching condition
     */
    List<MarkSettingVo> searchByCondition(MarkSetting condition);


    /**
     * get mark setting by rubric sub item id
     *
     * @param rubricItemId given rubric item id
     * @return mark setting with given rubric sub item i
     */

    MarkSettingVo getByRubricItemIdAndTemplateId(Integer rubricItemId,
                                                 Integer templateId);




    /**
     * get all valid mark settings
     *
     * @return List of mark settings
     */
    List<MarkSettingVo> getAllMarkSettings();

    /**
     * update mark setting
     *
     * @param entity mark setting with new information
     */
    void updateById(MarkSetting entity);

    /**
     * delete mark setting by given id
     *
     * @param id given mark setting id
     */
    void deleteById(Integer id);
}
