package com.feredback.feredback_backend.service;

import com.feredback.feredback_backend.entity.vo.RubricItemVo;
import com.feredback.feredback_backend.entity.vo.TemplateVo;

import java.util.List;

/**
 * @program: FE-Redback
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2022-04-18 00:11
 **/
public interface ITemplateService {
    void createTemplate(TemplateVo template, Integer projectId);


    Integer addRubricItemToTemplate(List<Integer> rubricItemIds, Integer templateId);

    List<TemplateVo> getAllTemplates(Integer subjectId);

    List<RubricItemVo> getAllRubricItemsInTemplate(Integer templateId);

    void updateTemplate(TemplateVo template);

    TemplateVo getTemplateById(Integer id);

    void deleteTemplateById(Integer id);

    void connectTemplateToProject(Integer projectId, Integer templateId);

    TemplateVo showTemplateOfProject(Integer projectId);

    void removeRubricItemFromTemplate(Integer templateId, Integer rubricItemId);
}
