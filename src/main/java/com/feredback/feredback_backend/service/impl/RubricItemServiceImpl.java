package com.feredback.feredback_backend.service.impl;

import com.feredback.feredback_backend.entity.Comment;
import com.feredback.feredback_backend.entity.RubricItem;
import com.feredback.feredback_backend.entity.RubricSubItem;
import com.feredback.feredback_backend.entity.vo.CommentVo;
import com.feredback.feredback_backend.entity.vo.RubricItemVo;
import com.feredback.feredback_backend.entity.vo.RubricSubItemVo;
import com.feredback.feredback_backend.mapper.CommentMapper;
import com.feredback.feredback_backend.mapper.RubricItemMapper;
import com.feredback.feredback_backend.mapper.RubricSubItemMapper;
import com.feredback.feredback_backend.service.ICommentService;
import com.feredback.feredback_backend.service.IRubricItemService;
import com.feredback.feredback_backend.service.IRubricSubItemService;
import com.feredback.feredback_backend.service.ex.DataModificationException;
import com.feredback.feredback_backend.service.ex.EmptyColumnException;
import com.feredback.feredback_backend.service.ex.FileTypeException;
import com.feredback.feredback_backend.util.CsvUtils;
import com.feredback.feredback_backend.util.ResultUtils;
import de.siegmar.fastcsv.reader.CsvContainer;
import de.siegmar.fastcsv.reader.CsvRow;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: FE-Redback
 * @description: Service implement class of rubric item
 * @author: Xun Zhang (854776)
 * @date: 2022/5/7
 **/
@Service
public class RubricItemServiceImpl implements IRubricItemService {
    @Resource
    private RubricItemMapper rubricItemMapper;

    @Resource
    private IRubricSubItemService subItemService;

    @Resource
    private ICommentService commentService;

    @Resource
    private RubricSubItemMapper rubricSubItemMapper;

    @Resource
    private CommentMapper commentMapper;


    /**
     * insert rubric item
     *
     * @param rubricItemVo given rubric item
     */
    @Override
    public void createRubricItem(RubricItemVo rubricItemVo) {
        Date current = new Date();
        RubricItem rubricItem = rubricItemVo.getRubricItem();
        rubricItem.setCreateTime(current);
        rubricItem.setUpdateTime(current);

        int rowsAffected = rubricItemMapper.insertRubricItem(rubricItem);
        Integer rubricItemId = rubricItem.getId();
        List<RubricSubItemVo> rubricSubItemVos = rubricItemVo.getRubricSubItems();
        insertSubItemsToItem(rubricItemId, rubricSubItemVos);
        if (!ResultUtils.singleCheck(rowsAffected)) {
            throw new DataModificationException("There is an error adding this rubric item");
        }
    }

    private void insertSubItemsToItem(Integer rubricItemId, List<RubricSubItemVo> rubricSubItemVos) {
        if (rubricSubItemVos != null) {
            for (RubricSubItemVo rubricSubItemVo : rubricSubItemVos) {
                RubricSubItem rubricSubItem = rubricSubItemVo.getRubricSubItem();
                rubricSubItem.setRubricItemId(rubricItemId);
                subItemService.createRubricSubItem(rubricSubItem);
                List<CommentVo> comments = rubricSubItemVo.getComments();
                for (CommentVo commentVo : comments) {
                    Comment comment = new Comment();
                    comment.setContent(commentVo.getContent());
                    int level = 0;
                    if ("positive".equals(commentVo.getLevel())) {
                        level = 1;
                    }
                    else if ("neutral".equals(commentVo.getLevel())) {
                        level = 0;
                    }
                    else if ("negative".equals(commentVo.getLevel())) {
                        level = -1;
                    }
                    comment.setLevel(level);
                    commentService.createComment(comment);
                    subItemService.addComment(rubricSubItem.getId(),comment.getId());
                }
            }
        }
    }

    /**
     * update rubric item
     *
     * @param rubricItemVo rubric item with new information
     */
    @Override
    public void updateById(RubricItemVo rubricItemVo) {
        Date current = new Date();
        RubricItem rubricItem = rubricItemVo.getRubricItem();
        rubricItem.setUpdateTime(current);
        rubricItemMapper.updateRubricItemById(rubricItem);
        List<RubricSubItem> rubricSubItems = rubricSubItemMapper.
                findRubricSubItemsByItemId(rubricItem.getId());
        for (RubricSubItem rubricSubItem : rubricSubItems) {
            Integer rubricSubItemId = rubricSubItem.getId();
            commentMapper.clearCommentInSubItem(rubricSubItemId);
        }
        rubricItemMapper.clearAllSubItems(rubricItem.getId());
        insertSubItemsToItem(rubricItem.getId(),rubricItemVo.getRubricSubItems());
    }

    @Override
    public Integer addItemsByCsv(MultipartFile file, Integer subjectId) {
        if (!CsvUtils.isCsvFile(file)) {
            throw new FileTypeException("Please ensure you're submitting a csv file");
        }
        int count = 0;
        CsvContainer csvContainer = CsvUtils.readFromMultipartFile(file);
        for (CsvRow row : csvContainer.getRows()) {
            String sectionTitle = row.getField("Section title").trim();
            String subSection = row.getField("Sub-section").trim();
            String level = row.getField("Level").trim();
            String longText = row.getField("Long text").trim();
            //Checking if any cell from this row is empty
            if (sectionTitle.isEmpty() || subSection.isEmpty() ||
                    level.isEmpty() || longText.isEmpty()) {
                throw new EmptyColumnException("Row " + row.getOriginalLineNumber()
                        + " contains empty cell, please check again");
            }
            RubricItem rubricItemFound = rubricItemMapper.findRubricItemByNameAndSubjectId(sectionTitle, subjectId);
            RubricItem rubricItem = new RubricItem();
            RubricSubItem rubricSubItem = new RubricSubItem();
            if (rubricItemFound == null) {
                Date current = new Date();
                rubricItem.setName(sectionTitle);
                rubricItem.setCreateTime(current);
                rubricItem.setUpdateTime(current);
                rubricItem.setSubjectId(subjectId);
                rubricItemMapper.insertRubricItem(rubricItem);
            }
            int rubricItemId;
            if (rubricItemFound == null) {
                rubricItemId = rubricItem.getId();
            } else {
                rubricItemId = rubricItemFound.getId();
            }
            RubricSubItem rubricSubItemFound = rubricSubItemMapper.findRubricSubItemsByNameAndItemId(rubricItemId, subSection);
            if (rubricSubItemFound == null) {
                rubricSubItem.setRubricItemId(rubricItemId);
                rubricSubItem.setName(subSection);
                subItemService.createRubricSubItem(rubricSubItem);
            }
            int rubricSubItemId;
            if (rubricSubItemFound == null) {
                rubricSubItemId = rubricSubItem.getId();
            } else {
                rubricSubItemId = rubricSubItemFound.getId();
            }
            List<Comment> comments = commentMapper.findCommentByRubricSubItemId(rubricSubItemId);
            List<String> commentContentsInSubItem = new ArrayList<>();
            for (Comment comment : comments) {
                commentContentsInSubItem.add(comment.getContent());
            }
            if (!commentContentsInSubItem.contains(longText)) {
                Comment comment = new Comment();
                comment.setContent(longText);
                int commentLevel = 0;
                int i = Integer.parseInt(level);
                if (i == 3) {
                    commentLevel = 1;
                }
                else if (i == 2) {
                    commentLevel = 0;
                }
                else if (i == 1) {
                    commentLevel = -1;
                }
                comment.setLevel(commentLevel);
                commentService.createComment(comment);
                subItemService.addComment(rubricSubItemId,comment.getId());
                count++;
            }
        }


        return count;
    }

    /**
     * get rubric item by its id
     *
     * @param id given rubric item id
     * @return rubric item with given id; or null if not find
     */
    @Override
    public RubricItem getById(Integer id) {
        return rubricItemMapper.findRubricItemById(id);
    }

    /**
     * get rubric items by template id
     *
     * @param templateId given template id
     * @return List of rubric items with given template id
     */
    @Override
    public List<RubricItem> getByTemplateId(Integer templateId) {
        return rubricItemMapper.findRubricItemsByTemplateId(templateId);
    }

    /**
     * search rubric items by name (key word)
     *
     * @param name key word of name
     * @return List of rubric items with name including given key word
     */
    @Override
    public List<RubricItem> searchByName(String name) {
        return rubricItemMapper.findRubricItemByName(name);
    }

    /**
     * get all valid rubric items
     *
     * @return List of rubric items
     * @param subjectId
     */
    @Override
    public List<RubricItemVo> getAllRubricItems(Integer subjectId) {
        List<RubricItemVo> rubricItemVos = new ArrayList<>();
        List<RubricItem> rubricItems = rubricItemMapper.findAllRubricItems(subjectId);
        for (RubricItem rubricItem : rubricItems) {
            RubricItemVo rubricItemVo = new RubricItemVo();
            rubricItemVo.setRubricItem(rubricItem);
            List<RubricSubItem> rubricSubItems = rubricSubItemMapper.
                    findRubricSubItemsByItemId(rubricItem.getId());
            List<RubricSubItemVo> rubricSubItemVos = new ArrayList<>();

            for (RubricSubItem rubricSubItem : rubricSubItems) {
                List<Comment> comments = commentMapper.findCommentByRubricSubItemId(rubricSubItem.getId());
                List<CommentVo> commentVos = new ArrayList<>();
                for (Comment comment: comments) {
                    commentVos.add(new CommentVo(comment));
                }
                RubricSubItemVo rubricSubItemVo = new RubricSubItemVo();
                rubricSubItemVo.setRubricSubItem(rubricSubItem);
                rubricSubItemVo.setComments(commentVos);
                rubricSubItemVos.add(rubricSubItemVo);
            }
            rubricItemVo.setRubricSubItems(rubricSubItemVos);
            rubricItemVos.add(rubricItemVo);
        }
        return rubricItemVos;
    }



    /**
     * delete rubric item by given id
     *
     * @param id given rubric item id
     */
    @Override
    public void deleteById(Integer id) {
        Date current = new Date();

        int rowsAffected = rubricItemMapper.deleteRubricItemById(id, current);
        List<RubricSubItem> subItems = subItemService.getByRubricItemId(id);
        for (RubricSubItem subItem : subItems) {

            subItemService.deleteById(subItem.getId());
        }
        rubricItemMapper.removeRubricItemFromAllTemplates(id,current);

        if (!ResultUtils.singleCheck(rowsAffected)) {
            throw new DataModificationException("There is an error deleting this rubric item");
        }
    }


}
