package com.feredback.feredback_backend.service;

import com.feredback.feredback_backend.entity.RubricSubItem;
import com.feredback.feredback_backend.entity.vo.RubricSubItemVo;

import java.util.List;

/**
 * @program: FE-Redback
 * @description: Service interface of rubric sub item
 * @author: Xun Zhang (854776)
 * @date: 2022/5/8
 **/
public interface IRubricSubItemService {
    /**
     * insert rubric sub item
     *
     * @param entity given rubric sub item
     */

    void createRubricSubItem(RubricSubItem entity);

    /**
     * add comment to this rubric sub item
     *
     * @param id id of rubric sub item
     * @param commentId if of comment to add
     */
    void addComment(Integer id, Integer commentId);

    /**
     * get rubric sub item by its id
     *
     * @param id given rubric sub item id
     * @return rubric sub item with given id; or null if not find
     */
    RubricSubItem getById(Integer id);

    /**
     * get rubric sub items by item id
     *
     * @param rubricItemId given rubric item id
     * @return List of rubric items with given rubric item id
     */
    List<RubricSubItem> getByRubricItemId(Integer rubricItemId);

    /**
     * search rubric sub items by name (key word)
     *
     * @param name key word of name
     * @return List of rubric sub items with name including given key word
     */
    List<RubricSubItem> searchByName(String name);

    /**
     * get all valid rubric sub items
     *
     * @return List of rubric sub items
     */
    List<RubricSubItem> getAllRubricItems();

    /**
     * update rubric sub item
     *
     * @param entity rubric sub item with new information
     */
    void updateById(RubricSubItem entity);

    /**
     * delete rubric sub item by given id
     *
     * @param id given rubric sub item id
     */
    void deleteById(Integer id);
}
