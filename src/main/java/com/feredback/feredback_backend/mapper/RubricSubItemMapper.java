package com.feredback.feredback_backend.mapper;

import com.feredback.feredback_backend.entity.RubricSubItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

/**
 * @program: FE-Redback
 * @description: mapper class of rubric sub item
 * @author: Xun Zhang (854776)
 * @date: 2022/5/8
 **/
@Mapper
public interface RubricSubItemMapper {
    /**
     * create a new rubric sub item into database
     *
     * @param entity new rubric sub item
     * @return number of modified line in database
     */
    int insertRubricSubItem(RubricSubItem entity);

    /**
     * add comment relationship to mid table
     *
     * @param id id of rubric sub item
     * @param commentId id of comment
     * @param current current datetime
     * @return number of modified line in database
     */
    int addComment(Integer id, Integer commentId, Date current);

    /**
     * find the comment relationship between given rubric sub item id and comment id
     *
     * @param id id of rubric sub item
     * @param commentId id of comment
     * @return count of comment relations with given ids
     */
    int isCommentExist(Integer id, Integer commentId);

    /**
     * retrieve the specific rubric sub item by given id
     *
     * @param id id of rubric sub item
     * @return the rubric sub item with given id
     */
    RubricSubItem findRubricSubItemById(Integer id);

    /**
     * retrieve the list of rubric sub items by given name key word
     *
     * @param name key word of name of rubric item
     * @return the list of rubric sub items with name include given key word
     */
    List<RubricSubItem> findRubricSubItemByName(String name);

    /**
     * retrieve all rubric sub items by given rubric item id
     *
     * @param rubricItemId given rubric item id
     * @return the rubric sub items with given rubric item id
     */
    List<RubricSubItem> findRubricSubItemsByItemId(Integer rubricItemId);

    /**
     * retrieve all rubric sub items
     *
     * @return the list of all rubric sub items
     */
    List<RubricSubItem> findAllRubricSubItems();

    /**
     * update the rubric sub item
     *
     * @param entity rubric sub item with new content
     * @return number of modified line in database
     */
    int updateRubricSubItemById(RubricSubItem entity);

    /**
     * delete the rubric sub item by given id
     * @param id given id of the rubric sub item to delete
     * @return number of modified line in database
     */
    int deleteRubricSubItemById(Integer id, Date updateTime);

    int removeCommentsFromSubItem(Integer subItemId, Date updateTime);

    RubricSubItem findRubricSubItemsByNameAndItemId(Integer rubricItemId, String name);
}
