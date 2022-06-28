package com.feredback.feredback_backend.mapper;

import com.feredback.feredback_backend.entity.Template;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

/**
 * @program: FE-Redback
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2022-04-18 00:14
 **/
@Mapper
public interface TemplateMapper {
    Integer insertTemplate(Template template);

    Template findTemplateByName(String templateName);

    Template findTemplateById(Integer id);

    List<Template> findAllTemplate(Integer subjectId);

    Integer addRubricItemToTemplate(Integer rubricItemId, Integer templateId, Date createTime, Date updateTime);

    Integer findDuplicatedRubricItemInTemplate(Integer rubricItemId, Integer templateId);

    Integer findDuplicatedTemplateNameInSubject(String name, Integer subjectId);

    Integer updateTemplate(Template template);

    Integer deleteTemplateById(Integer id, Date updateTime);

    Integer connectTemplateToProject(Integer projectId, Integer templateId, Date updateTime);

    Integer removeTemplateFromProject(Integer projectId, Date updateTime);


    Integer removeRubricItemFromTemplate(Integer templateId, Integer rubricItemId, Date updateTime);

    void removeAllRubricItemsFromTemplate(Integer templateId, Date updateTime);
}
