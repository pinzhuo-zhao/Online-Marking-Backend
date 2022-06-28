package com.feredback.feredback_backend.mapper;

import com.feredback.feredback_backend.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

/**
 * @program: FE-Redback
 * @description: mapper class of comment
 * @author: Xun Zhang (854776)
 * @date: 2022/5/8
 **/
@Mapper
public interface CommentMapper {
    /**
     * create a new comment into database
     *
     * @param entity new comment
     * @return number of modified line in database
     */
    int insertComment(Comment entity);

    /**
     * retrieve the specific comment by given id
     *
     * @param id id of comment
     * @return the comment with given id
     */
    Comment findCommentById(Integer id);

    /**
     * retrieve the list of comments by given content key word
     *
     * @param content content key word of comment
     * @return the list of comments with name include given content key word
     */
    List<Comment> findCommentByContent(String content);

    /**
     * retrieve the list of comments by given level
     *
     * @param level level of comment
     * @return the list of comments with given level
     */
    List<Comment> findCommentByLevel(Integer level);

    /**
     * retrieve all comments by given rubric sub item id
     *
     * @param rubricSubItemId given rubric sub item id
     * @return the comments with given rubric sub item id
     */
    List<Comment> findCommentByRubricSubItemId(Integer rubricSubItemId);

    /**
     * retrieve all comments
     *
     * @return the list of all comments
     */
    List<Comment> findAllComments();

    /**
     * update the comment
     *
     * @param entity comment with new content
     * @return number of modified line in database
     */
    int updateCommentById(Comment entity);

    /**
     * delete the comment by given id
     * @param id given id of the comment to delete
     * @return number of modified line in database
     */
    int deleteCommentById(Integer id, Date updateTime);

    void clearCommentInSubItem(Integer rubricSubItemId);
}
