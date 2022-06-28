package com.feredback.feredback_backend.service.impl;

import com.feredback.feredback_backend.entity.Comment;
import com.feredback.feredback_backend.entity.vo.CommentVo;
import com.feredback.feredback_backend.mapper.CommentMapper;
import com.feredback.feredback_backend.service.ICommentService;
import com.feredback.feredback_backend.service.ex.DataModificationException;
import com.feredback.feredback_backend.util.ResultUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @program: FE-Redback
 * @description:
 * @author: Xun Zhang (854776)
 * @date: 2022/5/8
 **/
@Service
public class CommentServiceImpl implements ICommentService {
    @Resource
    CommentMapper mapper;

    /**
     * insert comment
     *
     * @param entity given comment
     */
    @Override
    public void createComment(Comment entity) {
        Date current = new Date();
        entity.setCreateTime(current);
        entity.setUpdateTime(current);

        int rowsAffected = mapper.insertComment(entity);
        if (!ResultUtils.singleCheck(rowsAffected)) {
            throw new DataModificationException("There is an error adding this comment");
        }
    }

    /**
     * get comment by its id
     *
     * @param id given comment id
     * @return comment with given id; or null if not find
     */
    @Override
    public CommentVo getById(Integer id) {
        Comment comment = mapper.findCommentById(id);
        if (Objects.isNull(comment)) {
            return null;
        }

        return new CommentVo(comment);
    }

    /**
     * get comments by given level
     *
     * @param level level of comment
     * @return the list of comments with given level
     */
    @Override
    public List<CommentVo> getByLevel(Integer level) {
        List<Comment> resultList = mapper.findCommentByLevel(level);
        List<CommentVo> output = new LinkedList<>();

        for (Comment entity: resultList) {
            output.add(new CommentVo(entity));
        }

        return output;
    }

    /**
     * search comments by key word of content
     *
     * @param content key word of comment content
     * @return List of comments with given key word of content
     */
    @Override
    public List<CommentVo> searchByContent(String content) {
        List<Comment> resultList = mapper.findCommentByContent(content);
        List<CommentVo> output = new LinkedList<>();

        for (Comment entity: resultList) {
            output.add(new CommentVo(entity));
        }

        return output;
    }

    /**
     * get comments by rubric sub item id
     *
     * @param rubricSubItemId given rubric sub item id
     * @return List of comments with given rubric sub item id
     */
    @Override
    public List<CommentVo> getByRubricSubItemId(Integer rubricSubItemId) {
        List<Comment> resultList = mapper.findCommentByRubricSubItemId(rubricSubItemId);
        List<CommentVo> output = new LinkedList<>();

        for (Comment entity: resultList) {
            output.add(new CommentVo(entity));
        }

        return output;
    }

    /**
     * get all valid comments
     *
     * @return List of comments
     */
    @Override
    public List<CommentVo> getAllComments() {
        List<Comment> resultList = mapper.findAllComments();
        List<CommentVo> output = new LinkedList<>();

        for (Comment entity: resultList) {
            output.add(new CommentVo(entity));
        }

        return output;
    }

    /**
     * update comment
     *
     * @param entity comment with new information
     */
    @Override
    public void updateById(Comment entity) {
        Date current = new Date();
        entity.setUpdateTime(current);

        int rowsAffected = mapper.updateCommentById(entity);
        if (!ResultUtils.singleCheck(rowsAffected)) {
            throw new DataModificationException("There is an error updating this comment");
        }
    }

    /**
     * delete comment by given id
     *
     * @param id given comment id
     */
    @Override
    public void deleteById(Integer id) {
        Date current = new Date();

        int rowsAffected = mapper.deleteCommentById(id, current);
        if (!ResultUtils.singleCheck(rowsAffected)) {
            throw new DataModificationException("There is an error deleting this comment");
        }
    }
}
