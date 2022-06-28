package com.feredback.feredback_backend.service.impl;

import com.feredback.feredback_backend.entity.Comment;
import com.feredback.feredback_backend.entity.RubricSubItem;
import com.feredback.feredback_backend.mapper.CommentMapper;
import com.feredback.feredback_backend.mapper.RubricSubItemMapper;
import com.feredback.feredback_backend.service.IRubricSubItemService;
import com.feredback.feredback_backend.service.ex.DataModificationException;
import com.feredback.feredback_backend.service.ex.InsertionDuplicatedException;
import com.feredback.feredback_backend.util.ResultUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @program: FE-Redback
 * @description: Service implement class of rubric sub item
 * @author: Xun Zhang (854776)
 * @date: 2022/5/8
 **/
@Service
public class RubricSubItemServiceImpl implements IRubricSubItemService {
    @Resource
    RubricSubItemMapper mapper;

    @Resource
    CommentMapper commentMapper;

    /**
     * insert rubric sub item
     *
     * @param entity given rubric sub item
     */
    @Override
    public void createRubricSubItem(RubricSubItem entity) {
        Date current = new Date();
        entity.setCreateTime(current);
        entity.setUpdateTime(current);

        int rowsAffected = mapper.insertRubricSubItem(entity);
        if (!ResultUtils.singleCheck(rowsAffected)) {
            throw new DataModificationException("There is an error adding this rubric sub item");
        }
    }

    /**
     * add comment to this rubric sub item
     *
     * @param id        id of rubric sub item
     * @param commentId if of comment to add
     */
    @Override
    public void addComment(Integer id, Integer commentId) {
        int count = mapper.isCommentExist(id, commentId);
        if (count > 0) {
            throw new InsertionDuplicatedException("Comment relation already exist.");
        }

        Date current = new Date();
        int rowsAffected = mapper.addComment(id, commentId, current);
        if (!ResultUtils.singleCheck(rowsAffected)) {
            throw new DataModificationException("There is an error adding comment to this rubric sub item");
        }
    }

    /**
     * get rubric sub item by its id
     *
     * @param id given rubric sub item id
     * @return rubric sub item with given id; or null if not find
     */
    @Override
    public RubricSubItem getById(Integer id) {
        return mapper.findRubricSubItemById(id);
    }

    /**
     * get rubric sub items by item id
     *
     * @param rubricItemId given rubric item id
     * @return List of rubric items with given rubric item id
     */
    @Override
    public List<RubricSubItem> getByRubricItemId(Integer rubricItemId) {
        return mapper.findRubricSubItemsByItemId(rubricItemId);
    }

    /**
     * search rubric sub items by name (key word)
     *
     * @param name key word of name
     * @return List of rubric sub items with name including given key word
     */
    @Override
    public List<RubricSubItem> searchByName(String name) {
        return mapper.findRubricSubItemByName(name);
    }

    /**
     * get all valid rubric sub items
     *
     * @return List of rubric sub items
     */
    @Override
    public List<RubricSubItem> getAllRubricItems() {
        return mapper.findAllRubricSubItems();
    }

    /**
     * update rubric sub item
     *
     * @param entity rubric sub item with new information
     */
    @Override
    public void updateById(RubricSubItem entity) {
        Date current = new Date();
        entity.setUpdateTime(current);

        int rowsAffected = mapper.updateRubricSubItemById(entity);
        if (!ResultUtils.singleCheck(rowsAffected)) {
            throw new DataModificationException("There is an error updating this rubric sub item");
        }
    }

    /**
     * delete rubric sub item by given id
     *
     * @param id given rubric sub item id
     */
    @Override
    public void deleteById(Integer id) {
        Date current = new Date();

        int rowsAffected = mapper.deleteRubricSubItemById(id, current);
        mapper.removeCommentsFromSubItem(id,current);
        List<Comment> comments = commentMapper.findCommentByRubricSubItemId(id);
        for (Comment comment : comments) {
            commentMapper.deleteCommentById(comment.getId(),current);
        }

        if (!ResultUtils.singleCheck(rowsAffected)) {
            throw new DataModificationException("There is an error deleting this rubric sub item");
        }
    }
}
