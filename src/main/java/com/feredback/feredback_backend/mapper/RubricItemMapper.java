package com.feredback.feredback_backend.mapper;

import com.feredback.feredback_backend.entity.RubricItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

/**
 * @program: FE-Redback
 * @description: mapper class of rubric item
 * @author: Pinzhuo Zhao (1043915); Xun Zhang (854776)
 * @create: 2022-04-18 01:13
 **/
@Mapper
public interface RubricItemMapper {
    /**
     * create a new rubric item into database
     * @param entity new rubric item
     * @return number of modified line in database
     */
    int insertRubricItem(RubricItem entity);

    /**
     * retrieve the specific rubric item by given id
     * @param id id of rubric item
     * @return the rubric item with given id
     */
    RubricItem findRubricItemById(Integer id);

    /**
     * retrieve the list of rubric items by given name key word
     * @param name key word of name of rubric item
     * @return the list of rubric items with name include given key word
     */
    List<RubricItem> findRubricItemByName(String name);

    /**
     * retrieve all rubric items by given template id
     * @param templateId given template id
     * @return the rubric items with given template id
     */
    List<RubricItem> findRubricItemsByTemplateId(Integer templateId);

    RubricItem findRubricItemByNameAndSubjectId(String name, Integer subjectId);

    /**
     * retrieve all rubric items
     *
     * @return the list of all rubric items
     * @param subjectId
     */
    List<RubricItem> findAllRubricItems(Integer subjectId);

    /**
     * update the rubric item
     * @param entity rubric item with new content
     * @return number of modified line in database
     */
    int updateRubricItemById(RubricItem entity);

    /**
     * delete the rubric item by given id
     * @param id given id of the rubric item to delete
     * @return number of modified line in database
     */
    int deleteRubricItemById(Integer id, Date updateTime);


    int removeRubricItemFromAllTemplates(Integer rubricItemId, Date updateTime);


    void clearAllSubItems(Integer rubricItemId);
}
