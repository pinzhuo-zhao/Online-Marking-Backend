package com.feredback.feredback_backend.controller;



import com.feredback.feredback_backend.entity.Candidate;
import com.feredback.feredback_backend.util.JsonResult;
import com.feredback.feredback_backend.util.SecurityContextUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.feredback.feredback_backend.service.ICandidateService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @program: FE-Redback*
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2022-04-03 23:31
 **/

@RestController
@RequestMapping("/api/candidates")
@CrossOrigin
public class CandidateController extends BaseController {

    @Autowired
    private ICandidateService candidateService;

    @ApiOperation("Form needs id, firstName, middleName(optional), lastName, email")
    @PostMapping("addToSubject/{subjectId}")
    public JsonResult addCandidate(@RequestBody Candidate candidate, @PathVariable Integer subjectId) {
        candidateService.addCandidate(candidate, subjectId);
        return JsonResult.ok();
    }

    @GetMapping("getAll/{subjectId}")
    public JsonResult getAllUsers(@PathVariable Integer subjectId) {
        List<Candidate> candidates = candidateService.getAllUsers(subjectId);
        JsonResult result = JsonResult.ok();
        result.data("candidates",candidates);
        return result;
    }

    @PostMapping("addByCsv/{subjectId}")
    public JsonResult addCandidateByCsv(@RequestParam("file") MultipartFile file, @PathVariable Integer subjectId) {
        Integer candidatesAdded = candidateService.addCandidateByCsv(file, subjectId);
        return JsonResult.ok().data("numberOfCandidatesAdded", candidatesAdded);
    }

    @ApiOperation(value = "Delete a candidate by setting its 'isDeleted' to 1 ")
    @PutMapping("delete/{id}")
    public JsonResult deleteCandidate(@PathVariable Integer id) {
        candidateService.deleteCandidateById(id);
        return JsonResult.ok();

    }

}
