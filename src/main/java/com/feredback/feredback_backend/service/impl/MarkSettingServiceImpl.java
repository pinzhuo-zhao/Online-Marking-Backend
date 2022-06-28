package com.feredback.feredback_backend.service.impl;

import com.feredback.feredback_backend.entity.MarkSetting;
import com.feredback.feredback_backend.entity.vo.MarkSettingVo;
import com.feredback.feredback_backend.mapper.MarkSettingMapper;
import com.feredback.feredback_backend.service.IMarkSettingService;
import com.feredback.feredback_backend.service.ex.DataModificationException;
import com.feredback.feredback_backend.util.ResultUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @program: FE-Redback
 * @description: Service implement class of mark setting
 * @author: Xun Zhang (854776)
 * @date: 2022/5/8
 **/
@Service
public class MarkSettingServiceImpl implements IMarkSettingService {
    @Resource
    MarkSettingMapper mapper;

    /**
     * insert mark setting
     *
     * @param entity given mark setting
     */
    @Override
    public void createMarkSetting(MarkSetting entity) {
        Date current = new Date();
        entity.setCreateTime(current);
        entity.setUpdateTime(current);

        int rowsAffected = mapper.insertMarkSetting(entity);
        if (!ResultUtils.singleCheck(rowsAffected)) {
            throw new DataModificationException("There is an error adding this mark setting");
        }
    }

    /**
     * get mark setting by its id
     *
     * @param id given mark setting id
     * @return mark setting with given id; or null if not find
     */
    @Override
    public MarkSettingVo getById(Integer id) {
        MarkSetting markSetting = mapper.findMarkSettingById(id);
        if (Objects.isNull(markSetting)) {
            return null;
        }

        return new MarkSettingVo(markSetting);
    }

    /**
     * search mark settings by searching condition
     *
     * @param condition searching condition of mark setting
     * @return List of mark settings with given searching condition
     */
    @Override
    public List<MarkSettingVo> searchByCondition(MarkSetting condition) {
        List<MarkSetting> resultList = mapper.findMarkSettingByCondition(condition);
        List<MarkSettingVo> output = new LinkedList<>();

        for (MarkSetting entity: resultList) {
            output.add(new MarkSettingVo(entity));
        }
        
        return output;
    }

    /**
     * get mark setting by rubric sub item id
     *
     * @param rubricItemId given rubric sub item id
     * @return mark setting with given rubric sub item id
     */
    @Override
    public MarkSettingVo getByRubricItemIdAndTemplateId(Integer rubricItemId,
                                                        Integer templateId) {
        MarkSetting markSetting = mapper.
                findMarkSettingByRubricItemIdAndTemplateId(rubricItemId,templateId);
        if (Objects.isNull(markSetting)) {
            return null;
        }

        return new MarkSettingVo(markSetting);
    }

    /**
     * get all valid mark settings
     *
     * @return List of mark settings
     */
    @Override
    public List<MarkSettingVo> getAllMarkSettings() {
        List<MarkSetting> resultList = mapper.findAllMarkSettings();
        List<MarkSettingVo> output = new LinkedList<>();

        for (MarkSetting entity: resultList) {
            output.add(new MarkSettingVo(entity));
        }

        return output;
    }

    /**
     * update mark setting
     *
     * @param entity mark setting with new information
     */
    @Override
    public void updateById(MarkSetting entity) {
        Date current = new Date();
        entity.setUpdateTime(current);

        int rowsAffected = mapper.updateMarkSettingById(entity);
        if (!ResultUtils.singleCheck(rowsAffected)) {
            throw new DataModificationException("There is an error updating this mark setting");
        }
    }

    /**
     * delete mark setting by given id
     *
     * @param id given mark setting id
     */
    @Override
    public void deleteById(Integer id) {
        Date current = new Date();

        int rowsAffected = mapper.deleteMarkSettingById(id, current);
        if (!ResultUtils.singleCheck(rowsAffected)) {
            throw new DataModificationException("There is an error deleting this mark setting");
        }
    }
}
