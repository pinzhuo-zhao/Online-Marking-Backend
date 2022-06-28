package com.feredback.feredback_backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.feredback.feredback_backend.entity.Subject;
import com.feredback.feredback_backend.service.ISubjectService;
import com.feredback.feredback_backend.util.JsonResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: FE-Redback
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2022-04-15 22:14
 **/
@RestController
@RequestMapping("api/subject")
@CrossOrigin
public class SubjectController extends BaseController {
    @Autowired
    ISubjectService subjectService;

    @ApiOperation("Form needs subjectCode 和 Subject Name")
    @PostMapping("admin/addSubject")
    public JsonResult addSubject(@RequestBody Subject subject) {
        subjectService.addSubject(subject);
        return JsonResult.ok();
    }

    @GetMapping("admin/getAllSubjects")
    public JsonResult getAllSubjects() {
        List<Subject> subjects = subjectService.getAllSubjects();
        JsonResult result = JsonResult.ok().data("allSubjects", subjects);
        return result;

    }


    @GetMapping("getMySubjects")
    public JsonResult getMySubjects() {
        List<Subject> mySubjects = subjectService.getMySubjects();
        return JsonResult.ok().data("mySubjects", mySubjects);
    }

    @GetMapping("getSubjectById/{subjectId}")
    public JsonResult getSubjectById(@PathVariable Integer subjectId) {
        Subject subject = subjectService.getSubjectById(subjectId);
        JsonResult result = JsonResult.ok().data("allSubjects", subject);
        return result;
    }

    @PutMapping("admin/deleteSubject/{subjectId}")
    public JsonResult removeRubricItemFromTemplate(@PathVariable Integer subjectId) {
        subjectService.deleteSubject(subjectId);
        return JsonResult.ok();
    }


}
