package com.feredback.feredback_backend.controller;

import com.feredback.feredback_backend.entity.vo.TemplateVo;
import com.feredback.feredback_backend.service.ITemplateService;
import com.feredback.feredback_backend.util.JsonResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: FE-Redback
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2022-04-18 00:10
 **/

@RestController
@RequestMapping("api/template")
@CrossOrigin
public class TemplateController extends BaseController {

    @Autowired
    ITemplateService templateService;

    @ApiOperation(value = "Check the example of implementation notes below", notes = "{ \"projectId\": 1, \"rubricItems\": [ { \"markSetting\": { \"increment\": 0.25, \"maximum\": 10, \"rubricItemId\": 1, \"weighting\": 20 }, \"rubricItem\": { \"id\": 1 }\n" +
            "}], \"template\": { \"description\": \"Assignment 1_Template\", \"name\": \"A1_T1\"} }")
    @PostMapping("createTemplate/{subjectId}")
    public JsonResult createTemplate(@RequestBody TemplateVo template,
                                     @PathVariable Integer subjectId) {
        templateService.createTemplate(template, subjectId);
        return JsonResult.ok();
    }

    @ApiOperation(value = "Form needs id templateName and templateDescription", notes = "{\"rubricItems\": [ { \"markSetting\": { \"increment\": 0.25, \"maximum\": 10, \"rubricItemId\": 1, \"weighting\": 20 }, \"rubricItem\": { \"id\": 1 } } ], \"template\": { \"id\":6, \"description\": \"Assignment 1_Template_NEW_Ver2\", \"name\": \"A1_T1\"} }")
    @PutMapping("updateTemplate")
    public JsonResult updateTemplateById (@RequestBody TemplateVo template) {
        templateService.updateTemplate(template);
        return JsonResult.ok();
    }

    @GetMapping("getAllTemplates/{subjectId}")
    public JsonResult getAllTemplates(@PathVariable Integer subjectId) {
        List<TemplateVo> templates = templateService.getAllTemplates(subjectId);
        return JsonResult.ok().data("templates",templates);
    }

    @GetMapping("getTemplateById/{templateId}")
    public JsonResult getTemplateById(@PathVariable Integer templateId) {
        TemplateVo templateVo = templateService.getTemplateById(templateId);
        return JsonResult.ok().data("templates",templateVo);
    }

    @ApiOperation("Form needs an array: []rubricItemsIds with path variable templateId")
    @PostMapping("addRubricItemsToTemplate/{templateId}")
    public JsonResult addRubricItemsToTemplate(@RequestBody List<Integer> rubricItemIds,
                                          @PathVariable Integer templateId) {
        Integer rowsAdded = templateService.addRubricItemToTemplate(rubricItemIds, templateId);
        return JsonResult.ok().data("recordsAdded", rowsAdded);
    }

    @PutMapping("removeRubricItemFromTemplate/{templateId}/{rubricItemId}")
    public JsonResult removeRubricItemFromTemplate(@PathVariable Integer templateId,
                                          @PathVariable Integer rubricItemId) {
        templateService.removeRubricItemFromTemplate(templateId, rubricItemId);
        return JsonResult.ok();
    }






    @ApiOperation(value = "Delete a template by setting its 'isDeleted' to 1 ")
    @PutMapping("delete/{id}")
    public JsonResult deleteTemplate(@PathVariable Integer id) {
        templateService.deleteTemplateById(id);
        return JsonResult.ok();

    }
}
