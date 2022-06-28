package com.feredback.feredback_backend.controller;

import com.feredback.feredback_backend.entity.MarkSetting;
import com.feredback.feredback_backend.entity.vo.MarkSettingVo;
import com.feredback.feredback_backend.service.IMarkSettingService;
import com.feredback.feredback_backend.util.JsonResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: FE-Redback
 * @description: Controller class of mark setting
 * @author: Xun Zhang (854776)
 * @date: 2022/5/8
 **/
@RestController
@RequestMapping("api/rubric/markSetting")
@CrossOrigin
public class MarkSettingController {
    @Resource
    IMarkSettingService service;

    @ApiOperation("表单需要maximum, weighting, increment")
    @PostMapping("createMarkSetting")
    public JsonResult createMarkSetting(@RequestBody MarkSetting entity) {
        service.createMarkSetting(entity);
        return JsonResult.ok().data("id", entity.getId());
    }

    @ApiOperation("根据maximum, weighting, increment查询")
    @PostMapping("searchMarkSettings/")
    public JsonResult searchMarkSettingsByCondition(@RequestBody MarkSetting condition) {
        List<MarkSettingVo> MarkSettings = service.searchByCondition(condition);
        return JsonResult.ok().data("markSettings", MarkSettings);
    }

    @ApiOperation("根据id查询单个mark setting")
    @GetMapping("getMarkSetting/{id}")
    public JsonResult getMarkSettingById(@PathVariable Integer id) {
        MarkSettingVo MarkSetting = service.getById(id);
        return JsonResult.ok().data("markSetting", MarkSetting);
    }

    @ApiOperation("提供templateId和rubricSubItemId")
    @GetMapping("getMarkSetting/Item/{templateId}/{rubricItemId}")
    public JsonResult getMarkSettingsByRubricItemId( @PathVariable Integer templateId,
                                                     @PathVariable Integer rubricItemId) {
        MarkSettingVo MarkSettings = service.
                getByRubricItemIdAndTemplateId(rubricItemId,templateId);
        return JsonResult.ok().data("markSettings", MarkSettings);
    }

    @ApiOperation("查询所有mark settings")
    @GetMapping("getMarkSettings/all")
    public JsonResult getAllMarkSettings() {
        List<MarkSettingVo> MarkSettings = service.getAllMarkSettings();
        return JsonResult.ok().data("markSettings", MarkSettings);
    }

    @PutMapping("updateMarkSetting")
    public JsonResult updateMarkSettingById(@RequestBody MarkSetting entity) {
        service.updateById(entity);
        return JsonResult.ok();
    }

    @DeleteMapping("deleteMarkSetting/{id}")
    public JsonResult deleteMarkSettingById(@PathVariable Integer id) {
        service.deleteById(id);
        return JsonResult.ok();
    }
}
