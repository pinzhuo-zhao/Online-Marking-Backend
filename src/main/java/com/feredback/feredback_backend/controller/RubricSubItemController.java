package com.feredback.feredback_backend.controller;

import com.feredback.feredback_backend.entity.Comment;
import com.feredback.feredback_backend.entity.RubricSubItem;
import com.feredback.feredback_backend.entity.vo.CommentVo;
import com.feredback.feredback_backend.entity.vo.RubricSubItemVo;
import com.feredback.feredback_backend.service.ICommentService;
import com.feredback.feredback_backend.service.IRubricSubItemService;
import com.feredback.feredback_backend.util.JsonResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: FE-Redback
 * @description: Controller class of rubric sub item
 * @author: Xun Zhang (854776)
 * @date: 2022/5/8
 **/
@RestController
@RequestMapping("api/rubric/subItem")
@CrossOrigin
public class RubricSubItemController extends BaseController {
    @Resource
    private IRubricSubItemService rubricSubItemService;



    @Resource
    private ICommentService commentService;


    @ApiOperation(value = "", notes = "{\n" +
            "  \"comments\": [\n" +
            "    {\n" +
            "      \"content\": \"Your correctness is excellent\",\n" +
            "      \"level\": \"positive\"\n" +
            "    },\n" +
            "  {\n" +
            "      \"content\": \"Your correctness is average\",\n" +
            "      \"level\": \"neutral\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"rubricSubItem\": {\n" +
            "    \"name\": \"Correctness\",\n" +
            "    \"rubricItemId\": 5\n" +
            "  }\n" +
            "}")
    @PostMapping("createRubricSubItem")
    public JsonResult createRubricSubItem(@RequestBody RubricSubItemVo rubricSubItemVo) {
        rubricSubItemService.createRubricSubItem(rubricSubItemVo.getRubricSubItem());
        List<CommentVo> commentVos = rubricSubItemVo.getComments();
        if (commentVos != null) {
            for (CommentVo commentVo : commentVos) {
                Comment comment = new Comment();
                comment.setContent(commentVo.getContent());
                int level = 0;
                if ("positive".equals(commentVo.getLevel())) {
                    level = 1;
                } else if ("neutral".equals(commentVo.getLevel())) {
                    level = 0;
                } else if ("negative".equals(commentVo.getLevel())) {
                    level = -1;
                }
                comment.setLevel(level);
                commentService.createComment(comment);
                rubricSubItemService.addComment(rubricSubItemVo.getRubricSubItem().getId(), comment.getId());
            }
        }

        return JsonResult.ok().data("id", rubricSubItemVo.getRubricSubItem().getId());
    }

    @ApiOperation("提供名字关键字")
    @PostMapping("searchRubricSubItems/{name}")
    public JsonResult searchRubricSubItemsByName(@PathVariable String name) {
        List<RubricSubItem> rubricSubItems = rubricSubItemService.searchByName(name);
        return JsonResult.ok().data("rubricSubItems", rubricSubItems);
    }

    @ApiOperation(value = "", notes = "[\n" +
            "  {\n" +
            "    \"content\": \"Your Correctness is below average\",\n" +
            "    \"level\": \"negative\"\n" +
            "  },\n" +
            "{\n" +
            "    \"content\": \"Your Correctness is very good\",\n" +
            "    \"level\": \"positive\"\n" +
            "  }\n" +
            "\n" +
            "]")
    @PostMapping("manageRubricSubItems/{rubricSubItemId}/addComment")
    public JsonResult addCommentToRubricSubItem(@RequestBody List<CommentVo> commentVos, @PathVariable Integer rubricSubItemId) {
        for (CommentVo commentVo : commentVos) {
            Comment comment = new Comment();
            comment.setContent(commentVo.getContent());
            int level = 0;
            if ("positive".equals(commentVo.getLevel())) {
                level = 1;
            } else if ("neutral".equals(commentVo.getLevel())) {
                level = 0;
            } else if ("negative".equals(commentVo.getLevel())) {
                level = -1;
            }
            comment.setLevel(level);
            commentService.createComment(comment);
            rubricSubItemService.addComment(rubricSubItemId, comment.getId());
        }
        return JsonResult.ok();
    }

    @ApiOperation("根据id查询单个rubric sub item")
    @GetMapping("getRubricSubItem/{id}")
    public JsonResult getRubricSubItemById(@PathVariable Integer id) {
        RubricSubItem rubricSubItem = rubricSubItemService.getById(id);
        return JsonResult.ok().data("rubricSubItem", rubricSubItem);
    }

    @ApiOperation("提供rubricItemId")
    @GetMapping("getRubricSubItems/{rubricItemId}")
    public JsonResult getRubricSubItemsByTemplateId(@PathVariable Integer rubricItemId) {
        List<RubricSubItem> rubricSubItems = rubricSubItemService.getByRubricItemId(rubricItemId);
        return JsonResult.ok().data("rubricSubItems", rubricSubItems);
    }

    @ApiOperation("查询所有rubric sub items")
    @GetMapping("getRubricSubItems/all")
    public JsonResult getAllRubricSubItems() {
        List<RubricSubItem> rubricSubItems = rubricSubItemService.getAllRubricItems();
        return JsonResult.ok().data("rubricSubItems", rubricSubItems);
    }

    @ApiOperation(value = "", notes = "{\n" +
            "  \"id\" : 8,\n" +
            "  \"name\": \"Correctnesss\",\n" +
            "  \"rubricItemId\": 5\n" +
            "}")
    @PutMapping("updateRubricSubItem")
    public JsonResult updateRubricSubItemById(@RequestBody RubricSubItem entity) {
        rubricSubItemService.updateById(entity);
        return JsonResult.ok();
    }

    @DeleteMapping("deleteRubricSubItem/{id}")
    public JsonResult deleteRubricSubItemById(@PathVariable Integer id) {
        rubricSubItemService.deleteById(id);
        return JsonResult.ok();
    }
}
