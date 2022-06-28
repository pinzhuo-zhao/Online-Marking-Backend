package com.feredback.feredback_backend.mapper;

import com.feredback.feredback_backend.entity.MarkSetting;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

/**
 * @program: FE-Redback
 * @description: mapper class of rubric mark setting
 * @author: Xun Zhang (854776)
 * @date: 2022/5/8
 **/
@Mapper
public interface MarkSettingMapper {
    /**
     * create a new mark setting into database
     *
     * @param entity new mark setting
     * @return number of modified line in database
     */
    int insertMarkSetting(MarkSetting entity);

    /**
     * retrieve the specific mark setting by given id
     *
     * @param id id of mark setting
     * @return the mark setting with given id
     */
    MarkSetting findMarkSettingById(Integer id);

    /**
     * retrieve the list of mark settings by given searching condition
     *
     * @param condition searching condition of mark setting
     * @return the list of mark settings with given searching condition
     */
    List<MarkSetting> findMarkSettingByCondition(MarkSetting condition);

    /**
     * retrieve mark setting by given rubric sub item id
     *
     * @param rubricItemId given rubric sub item id
     * @return the mark setting with given rubric sub item id
     */
    MarkSetting findMarkSettingByRubricItemIdAndTemplateId(Integer rubricItemId, Integer templateId);

    /**
     * retrieve all mark settings
     *
     * @return the list of all mark settings
     */
    List<MarkSetting> findAllMarkSettings();

    /**
     * update the mark setting
     *
     * @param entity mark setting with new content
     * @return number of modified line in database
     */
    int updateMarkSettingById(MarkSetting entity);

    /**
     * delete the mark setting by given id
     * @param id given id of the mark setting to delete
     * @return number of modified line in database
     */
    int deleteMarkSettingById(Integer id, Date updateTime);


    void deleteMarkSettingByTemplateIdAndItemId(Integer templateId, Integer rubricItemId);
}
