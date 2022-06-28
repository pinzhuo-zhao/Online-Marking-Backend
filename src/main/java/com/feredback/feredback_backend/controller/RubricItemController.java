package com.feredback.feredback_backend.controller;

import com.feredback.feredback_backend.entity.RubricItem;
import com.feredback.feredback_backend.entity.vo.RubricItemVo;
import com.feredback.feredback_backend.service.IRubricItemService;
import com.feredback.feredback_backend.util.JsonResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: FE-Redback
 * @description: Controller class of rubric item
 * @author: Xun Zhang (854776)
 * @date: 2022/5/7
 **/
@RestController
@RequestMapping("api/rubric/item")
@CrossOrigin
public class RubricItemController extends BaseController {
    @Resource
    IRubricItemService rubricItemService;

    @ApiOperation(value = "",notes = "{ \"rubricItem\": { \"name\": \"Presentation Structure\", \"subjectId\":1 }, \"rubricSubItems\": [ { \"comments\": [ { \"content\": \"Your Structure is OK\", \"level\": \"neutral\" } ], \"rubricSubItem\": { \"name\": \"Main Body of the Presentation\" } }, { \"comments\": [ { \"content\": \"Your Introduction is awesome\", \"level\": \"positive\" }, { \"content\": \"Your Introduction is not good enough\", \"level\": \"negative\" } ], \"rubricSubItem\": { \"name\": \"Introduction\" } } ] }")
    @PostMapping("createRubricItem")
    public JsonResult createRubricItem(@RequestBody RubricItemVo entity) {
        rubricItemService.createRubricItem(entity);
        return JsonResult.ok().data("id", entity.getRubricItem().getId());
    }

    @PostMapping("addItemsByCsv/{subjectId}")
    public JsonResult addByCsv(@RequestParam("file") MultipartFile file, @PathVariable Integer subjectId) {
        Integer candidatesAdded = rubricItemService.addItemsByCsv(file, subjectId);
        return JsonResult.ok().data("numberOfItemsAdded", candidatesAdded);
    }

    @ApiOperation("提供名字关键字")
    @PostMapping("searchRubricItems/{name}")
    public JsonResult searchRubricItemsByName(@PathVariable String name) {
        List<RubricItem> rubricItems = rubricItemService.searchByName(name);
        return JsonResult.ok().data("rubricItems", rubricItems);
    }

    @ApiOperation("根据id查询单个rubric item")
    @GetMapping("getRubricItem/{id}")
    public JsonResult getRubricItemById(@PathVariable Integer id) {
        RubricItem rubricItem = rubricItemService.getById(id);
        return JsonResult.ok().data("rubricItem", rubricItem);
    }

    @ApiOperation("提供templateId")
    @GetMapping("getRubricItems/{templateId}")
    public JsonResult getRubricItemsByTemplateId(@PathVariable Integer templateId) {
        List<RubricItem> rubricItems = rubricItemService.getByTemplateId(templateId);
        return JsonResult.ok().data("rubricItems", rubricItems);
    }

    @ApiOperation("查询所有rubric item")
    @GetMapping("getRubricItems/all/{subjectId}")
    public JsonResult getAllRubricItems(@PathVariable Integer subjectId) {
        List<RubricItemVo> rubricItems = rubricItemService.getAllRubricItems(subjectId);
        return JsonResult.ok().data("rubricItems", rubricItems);
    }

    @ApiOperation(value = "", notes = "{ \"rubricItem\": { \"name\": \"Presentation Structure_new\", \"subjectId\":1, \"id\":8 }, \"rubricSubItems\": [ { \"comments\": [ { \"content\": \"Your Structure is OK\", \"level\": \"neutral\" } ], \"rubricSubItem\": { \"name\": \"Main Body of the Presentation_new\" } }, { \"comments\": [ { \"content\": \"Your Introduction is awesome\", \"level\": \"positive\" }, { \"content\": \"Your Introduction is not good enough_new\", \"level\": \"negative\" } ], \"rubricSubItem\": { \"name\": \"Introduction\" } } ] }")
    @PutMapping("updateRubricItem")
    public JsonResult updateRubricItemById(@RequestBody RubricItemVo entity) {
        rubricItemService.updateById(entity);
        return JsonResult.ok();
    }


    @DeleteMapping("deleteRubricItem/{id}")
    public JsonResult deleteRubricItemById(@PathVariable Integer id) {
        rubricItemService.deleteById(id);
        return JsonResult.ok();
    }
}
