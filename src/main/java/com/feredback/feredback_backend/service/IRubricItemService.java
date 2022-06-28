package com.feredback.feredback_backend.service;

import com.feredback.feredback_backend.entity.RubricItem;
import com.feredback.feredback_backend.entity.vo.RubricItemVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @program: FE-Redback
 * @description: Service interface of rubric item
 * @author: Xun Zhang (854776)
 * @date: 2022/5/6
 **/
public interface IRubricItemService {
    /**
     * insert rubric item
     *
     * @param entity given rubric item
     */
    void createRubricItem(RubricItemVo entity);

    /**
     * get rubric item by its id
     *
     * @param id given rubric item id
     * @return rubric item with given id; or null if not find
     */
    RubricItem getById(Integer id);

    /**
     * get rubric items by template id
     *
     * @param templateId given template id
     * @return List of rubric items with given template id
     */
    List<RubricItem> getByTemplateId(Integer templateId);

    /**
     * search rubric items by name (key word)
     *
     * @param name key word of name
     * @return List of rubric items with name including given key word
     */
    List<RubricItem> searchByName(String name);

    /**
     * get all valid rubric items
     *
     * @return List of rubric items
     * @param subjectId
     */
    List<RubricItemVo> getAllRubricItems(Integer subjectId);

    /**
     * update rubric item
     *
     * @param entity rubric item with new information
     */
    void updateById(RubricItemVo entity);

    /**
     * delete rubric item by given id
     *
     * @param id given rubric item id
     */
    void deleteById(Integer id);

    Integer addItemsByCsv(MultipartFile file, Integer subjectId);
}
