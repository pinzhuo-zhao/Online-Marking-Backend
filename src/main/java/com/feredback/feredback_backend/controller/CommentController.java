package com.feredback.feredback_backend.controller;

import com.feredback.feredback_backend.entity.Comment;
import com.feredback.feredback_backend.entity.vo.CommentVo;
import com.feredback.feredback_backend.service.ICommentService;
import com.feredback.feredback_backend.util.JsonResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: FE-Redback
 * @description:
 * @author: Xun Zhang (854776)
 * @date: 2022/5/8
 **/
@RestController
@RequestMapping("api/comment")
@CrossOrigin
public class CommentController {
    @Resource
    ICommentService service;

    @ApiOperation("表单需要content, level")
    @PostMapping("createComment")
    public JsonResult createComment(@RequestBody Comment entity) {
        service.createComment(entity);
        return JsonResult.ok().data("id", entity.getId());
    }

    @ApiOperation("根据content查询")
    @PostMapping("searchComments/content/{content}")
    public JsonResult searchCommentsByContent(@PathVariable String content) {
        List<CommentVo> comments = service.searchByContent(content);
        return JsonResult.ok().data("comments", comments);
    }

    @ApiOperation("提供level")
    @PostMapping("searchComments/level/{level}")
    public JsonResult getCommentsByLevel(@PathVariable Integer level) {
        List<CommentVo> comments = service.getByLevel(level);
        return JsonResult.ok().data("comments", comments);
    }

    @ApiOperation("根据id查询单个comment")
    @GetMapping("getComment/{id}")
    public JsonResult getCommentById(@PathVariable Integer id) {
        CommentVo comment = service.getById(id);
        return JsonResult.ok().data("comment", comment);
    }

    @ApiOperation("提供rubricSubItemId")
    @GetMapping("getComments/{rubricSubItemId}")
    public JsonResult getCommentsByRubricSubItemId(@PathVariable Integer rubricSubItemId) {
        List<CommentVo> comments = service.getByRubricSubItemId(rubricSubItemId);
        return JsonResult.ok().data("comments", comments);
    }

    @ApiOperation("查询所有comments")
    @GetMapping("getComments/all")
    public JsonResult getAllComments() {
        List<CommentVo> comments = service.getAllComments();
        return JsonResult.ok().data("comments", comments);
    }

    @PutMapping("updateComment")
    public JsonResult updateCommentById(@RequestBody Comment entity) {
        service.updateById(entity);
        return JsonResult.ok();
    }

    @DeleteMapping("deleteComment/{id}")
    public JsonResult deleteCommentById(@PathVariable Integer id) {
        service.deleteById(id);
        return JsonResult.ok();
    }
}
