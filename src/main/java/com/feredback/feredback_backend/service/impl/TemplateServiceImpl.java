package com.feredback.feredback_backend.service.impl;

import com.feredback.feredback_backend.entity.*;
import com.feredback.feredback_backend.entity.vo.*;
import com.feredback.feredback_backend.mapper.*;
import com.feredback.feredback_backend.service.ITemplateService;
import com.feredback.feredback_backend.service.ex.DataModificationException;
import com.feredback.feredback_backend.service.ex.InsertionDuplicatedException;
import com.feredback.feredback_backend.util.SecurityContextUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: FE-Redback
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2022-04-18 00:11
 **/
@Service
public class TemplateServiceImpl implements ITemplateService {
    @Resource
    private TemplateMapper templateMapper;

    @Resource
    private RubricItemMapper rubricItemMapper;

    @Resource
    private RubricSubItemMapper rubricSubItemMapper;

    @Resource
    private ProjectMapper projectMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private MarkSettingMapper markSettingMapper;

    @Resource
    private CommentMapper commentMapper;

    private void ifTemplateBelongsToSubject(Integer templateId) {
        Template templateFound = templateMapper.findTemplateById(templateId);
        Integer userId = SecurityContextUtil.getCurrentUser().getId();
        List<Integer> subjectIds = userMapper.findSubjectIdsByUserId(userId);
        if (templateFound == null) {
            throw new DataModificationException("The template cannot be found");
        }
        if (!subjectIds.contains(templateFound.getSubjectId())) {
            throw new DataModificationException("You do not have access to this project");
        }
    }

    private void ifProjectBelongsToSubject(Integer projectId) {
        Project projectFound = projectMapper.findProjectById(projectId);
        Integer userId = SecurityContextUtil.getCurrentUser().getId();
        List<Integer> subjectIds = userMapper.findSubjectIdsByUserId(userId);
        if (projectFound == null) {
            throw new DataModificationException("The project cannot be found");
        }
        if (!subjectIds.contains(projectFound.getSubjectId())) {
            throw new DataModificationException("You do not have access to this project");
        }
    }

    @Override
    public void createTemplate(TemplateVo templateVo, Integer subjectId) {
        //check if the subject actually belongs to the logged-in user
        Integer userId = SecurityContextUtil.getCurrentUser().getId();
        List<Integer> subjectIds = userMapper.findSubjectIdsByUserId(userId);
        if (!subjectIds.contains(subjectId)) {
            throw new DataModificationException("You do not have access to this project");
        }

        Template template = templateVo.getTemplate();
        //check if the name is duplicated or not
        Integer recordFound = templateMapper.
                findDuplicatedTemplateNameInSubject(template.getName(), subjectId);
        if (recordFound != 0) {
            throw new InsertionDuplicatedException("Template with the name: " + template.getName() + " already exists in this subject");
        }

        template.setSubjectId(subjectId);
        Date now = new Date();
        template.setCreateTime(now);
        template.setUpdateTime(now);
        Integer rowsAffected = templateMapper.insertTemplate(template);
        Integer templateId = template.getId();
        List<RubricItemVo> rubricItems = templateVo.getRubricItems();
        //add all rubricItems selected by the user to the template
        insertRubricItemsIntoTemplate(now, templateId, rubricItems);

        if (templateVo.getProjectId() != null) {
            templateMapper.connectTemplateToProject(templateVo.getProjectId(), templateId, now);
        }
        if (rowsAffected != 1) {
            throw new DataModificationException("There is an error adding this template");
        }

    }

    private void insertRubricItemsIntoTemplate(Date now, Integer templateId, List<RubricItemVo> rubricItems) {
        if (!rubricItems.isEmpty()) {
            List<Integer> rubricItemIds = new ArrayList<>();
            for (RubricItemVo rubricItemVo : rubricItems) {
                Integer rubricItemId = rubricItemVo.getRubricItem().getId();
                //insert mark setting according to user input
                MarkSettingVo markSettingVo = rubricItemVo.getMarkSetting();
                MarkSetting markSetting = new MarkSetting();
                Integer increment = 0;
                if (markSettingVo.getIncrement() == 0.25) {
                    increment = 0;
                } else if (markSettingVo.getIncrement() == 0.5) {
                    increment = 1;
                } else if (markSettingVo.getIncrement() == 1.0) {
                    increment = 2;
                }
                markSetting.setTemplateId(templateId);
                markSetting.setMaximum(markSettingVo.getMaximum());
                markSetting.setWeighting(markSettingVo.getWeighting());
                markSetting.setRubricItemId(markSettingVo.getRubricItemId());
                markSetting.setIncrement(increment);

                markSetting.setCreateTime(now);
                markSetting.setUpdateTime(now);
                rubricItemIds.add(rubricItemId);
                markSettingMapper.insertMarkSetting(markSetting);
            }
            addRubricItemToTemplate(rubricItemIds, templateId);
        }
    }

    @Override
    public void updateTemplate(TemplateVo templateVo) {
        Template template = templateVo.getTemplate();
        Integer templateId = template.getId();
        ifTemplateBelongsToSubject(templateId);
        Date updateTime = new Date();
        template.setUpdateTime(updateTime);
        templateMapper.updateTemplate(template);
        List<RubricItemVo> originalRubricItemVos = getAllRubricItemsInTemplate(templateId);
        for (RubricItemVo originalRubricItemVo : originalRubricItemVos) {
            markSettingMapper.deleteMarkSettingByTemplateIdAndItemId(
                    templateId, originalRubricItemVo.getRubricItem().getId());
        }
        templateMapper.removeAllRubricItemsFromTemplate(templateId,updateTime);
        List<RubricItemVo> rubricItems = templateVo.getRubricItems();
        insertRubricItemsIntoTemplate(updateTime,templateId,rubricItems);
    }

    @Override
    public List<TemplateVo> getAllTemplates(Integer subjectId) {
        Integer userId = SecurityContextUtil.getCurrentUser().getId();
        List<Integer> subjectIds = userMapper.findSubjectIdsByUserId(userId);
        if (!subjectIds.contains(subjectId)) {
            throw new DataModificationException("You do not have access to this subject");
        }
        List<Template> templates = templateMapper.findAllTemplate(subjectId);
        List<TemplateVo> templateVos = new ArrayList<>();
        for (Template template : templates) {
            List<RubricItemVo> rubricItems = getAllRubricItemsInTemplate(template.getId());
            TemplateVo templateVo = new TemplateVo();
            templateVo.setTemplate(template);
            templateVo.setRubricItems(rubricItems);
            templateVos.add(templateVo);
        }
        return templateVos;
    }



    @Override
    public List<RubricItemVo> getAllRubricItemsInTemplate(Integer templateId) {
        ifTemplateBelongsToSubject(templateId);
        List<RubricItem> rubricItemsInTemplate = rubricItemMapper.findRubricItemsByTemplateId(templateId);
        List<RubricItemVo> rubricItemVos = new ArrayList<>();
        for (RubricItem rubricItem : rubricItemsInTemplate) {
            RubricItemVo rubricItemVo = new RubricItemVo();
            rubricItemVo.setRubricItem(rubricItem);
            MarkSetting markSetting = markSettingMapper.
                    findMarkSettingByRubricItemIdAndTemplateId(rubricItem.getId(),templateId);
            if (markSetting != null) {
                rubricItemVo.setMarkSetting(new MarkSettingVo(markSetting));
            }
            List<RubricSubItem> subItemResultList = rubricSubItemMapper.findRubricSubItemsByItemId(rubricItem.getId());
            List<RubricSubItemVo> rubricSubItems = new ArrayList<>();
            for (RubricSubItem rubricSubItem: subItemResultList) {
                RubricSubItemVo rubricSubItemVo = new RubricSubItemVo();
                rubricSubItemVo.setRubricSubItem(rubricSubItem);
                List<Comment> commentResultList = commentMapper.findCommentByRubricSubItemId(rubricSubItem.getId());
                List<CommentVo> comments = new ArrayList<>();
                for (Comment comment: commentResultList) {
                    comments.add(new CommentVo(comment));
                }
                rubricSubItemVo.setComments(comments);
                rubricSubItems.add(rubricSubItemVo);
            }
            rubricItemVo.setRubricSubItems(rubricSubItems);
            rubricItemVos.add(rubricItemVo);
        }
        return rubricItemVos;
    }

    @Override
    public Integer addRubricItemToTemplate(List<Integer> rubricItemIds, Integer templateId) {
        if (rubricItemIds.isEmpty()) {
            throw new DataModificationException("No rubric items to be added, please try again");
        }
        if (templateMapper.findTemplateById(templateId) == null) {
            throw new DataModificationException("There is no such template");
        }
        int count = 0;
        for (Integer rubricItemId:rubricItemIds) {
            //skip the insertion if the rubric item is already in this template
            Integer recordsFound = templateMapper.findDuplicatedRubricItemInTemplate(
                    rubricItemId, templateId);
            if (recordsFound > 0) {
                continue;
            }

            //skip the insertion if there is no such candidate
            RubricItem rubricItemFound = rubricItemMapper.findRubricItemById(rubricItemId);
            if (rubricItemFound == null) {
                continue;
            }
            Date now = new Date();
            templateMapper.addRubricItemToTemplate(rubricItemId, templateId, now, now);
            count++;
        }
        return count;
    }

    @Override
    public void removeRubricItemFromTemplate(Integer templateId, Integer rubricItemId) {
        ifTemplateBelongsToSubject(templateId);
        Integer rowsAffected = templateMapper.
                removeRubricItemFromTemplate(templateId, rubricItemId, new Date());
        if (rowsAffected != 1) {
            throw new DataModificationException("There is an error removing rubric item from" +
                    " this template");
        }

    }



    @Override
    public TemplateVo getTemplateById(Integer id) {
        Template templateFound = templateMapper.findTemplateById(id);
        if (templateFound == null) {
            throw new DataModificationException("This template cannot be found");
        }
        List<RubricItemVo> allRubricItemsInTemplate = getAllRubricItemsInTemplate(id);
        TemplateVo templateVo = new TemplateVo();
        templateVo.setTemplate(templateFound);
        templateVo.setRubricItems(allRubricItemsInTemplate);
        return templateVo;
    }

    @Override
    public void deleteTemplateById(Integer id) {
        ifTemplateBelongsToSubject(id);
        Template templateFound = templateMapper.findTemplateById(id);
        if (templateFound == null) {
            throw new DataModificationException("This template cannot be found");
        }
        Date updateTime = new Date();
        //Clearing templateId of all projects currently using this template
        List<Project> projects = projectMapper.findProjectByTemplateId(id);
        for (Project project : projects) {
            templateMapper.removeTemplateFromProject(project.getId(),updateTime);
        }
        Integer rowsAffected = templateMapper.deleteTemplateById(id, updateTime);
        templateMapper.removeAllRubricItemsFromTemplate(id,updateTime);
        if (rowsAffected != 1) {
            throw new DataModificationException("There is an error deleting this template");
        }
    }

    @Override
    public void connectTemplateToProject(Integer projectId, Integer templateId) {
        ifProjectBelongsToSubject(projectId);
        ifTemplateBelongsToSubject(templateId);
        templateMapper.connectTemplateToProject(projectId, templateId, new Date());
    }

    @Override
    public TemplateVo showTemplateOfProject(Integer projectId) {
        ifProjectBelongsToSubject(projectId);
        Project project = projectMapper.findProjectById(projectId);
        Integer templateId = project.getTemplateId();
        Template template = templateMapper.findTemplateById(templateId);
        List<RubricItem> rubricItems = rubricItemMapper.findRubricItemsByTemplateId(templateId);
        List<RubricItemVo> rubricItemVos = new ArrayList<>();
        for (RubricItem rubricItem : rubricItems) {
            RubricItemVo rubricItemVo = new RubricItemVo();
            rubricItemVo.setRubricItem(rubricItem);
            MarkSetting markSetting = markSettingMapper.
                    findMarkSettingByRubricItemIdAndTemplateId(rubricItem.getId(),templateId);
            if (markSetting != null) {
                rubricItemVo.setMarkSetting(new MarkSettingVo(markSetting));
            }
            List<RubricSubItem> subItemResultList = rubricSubItemMapper.findRubricSubItemsByItemId(rubricItem.getId());
            List<RubricSubItemVo> rubricSubItems = new ArrayList<>();
            for (RubricSubItem rubricSubItem: subItemResultList) {
                RubricSubItemVo rubricSubItemVo = new RubricSubItemVo();
                rubricSubItemVo.setRubricSubItem(rubricSubItem);
                List<Comment> commentResultList = commentMapper.findCommentByRubricSubItemId(rubricSubItem.getId());
                List<CommentVo> comments = new ArrayList<>();
                for (Comment comment: commentResultList) {
                    comments.add(new CommentVo(comment));
                }
                rubricSubItemVo.setComments(comments);
                rubricSubItems.add(rubricSubItemVo);
            }
            rubricItemVo.setRubricSubItems(rubricSubItems);
            rubricItemVos.add(rubricItemVo);
        }

        TemplateVo templateVo = new TemplateVo();
        templateVo.setTemplate(template);
        templateVo.setDuration(project.getDuration());
        templateVo.setWarningTime(project.getWarningTime());
        templateVo.setRubricItems(rubricItemVos);

        return templateVo;
    }



}
