package com.feredback.feredback_backend.service;

import com.feredback.feredback_backend.entity.Comment;
import com.feredback.feredback_backend.entity.vo.CommentVo;

import java.util.List;

/**
 * @program: FE-Redback
 * @description: Service interface of comment
 * @author: Xun Zhang (854776)
 * @date: 2022/5/8
 **/
public interface ICommentService {
    /**
     * insert comment
     *
     * @param entity given comment
     */
    void createComment(Comment entity);

    /**
     * get comment by its id
     *
     * @param id given comment id
     * @return comment with given id; or null if not find
     */
    CommentVo getById(Integer id);

    /**
     * get comments by given level
     *
     * @param level level of comment
     * @return the list of comments with given level
     */
    List<CommentVo> getByLevel(Integer level);

    /**
     * search comments by key word of content
     *
     * @param content key word of comment content
     * @return List of comments with given key word of content
     */
    List<CommentVo> searchByContent(String content);

    /**
     * get comments by rubric sub item id
     *
     * @param rubricSubItemId given rubric sub item id
     * @return List of comments with given rubric sub item id
     */
    List<CommentVo> getByRubricSubItemId(Integer rubricSubItemId);

    /**
     * get all valid comments
     *
     * @return List of comments
     */
    List<CommentVo> getAllComments();

    /**
     * update comment
     *
     * @param entity comment with new information
     */
    void updateById(Comment entity);

    /**
     * delete comment by given id
     *
     * @param id given comment id
     */
    void deleteById(Integer id);
}
