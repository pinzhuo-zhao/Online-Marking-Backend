package com.feredback.feredback_backend.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.feredback.feredback_backend.entity.FeedbackTeamPersonal;
import com.feredback.feredback_backend.entity.vo.FeedbackCandidateVo;
import com.feredback.feredback_backend.entity.vo.FeedbackGeneralVo;
import com.feredback.feredback_backend.entity.vo.FeedbackItemVo;
import com.feredback.feredback_backend.entity.vo.FeedbackTeamVo;
import com.feredback.feredback_backend.service.IFeedbackService;
import com.feredback.feredback_backend.util.JsonResult;
import io.swagger.annotations.ApiOperation;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @program: FE-Redback
 * @description:
 * @author: Xun Zhang (854776)
 * @date: 2022/5/18
 **/
@RestController
@RequestMapping("api/feedback")
@CrossOrigin
public class FeedbackController extends BaseController{
    @Resource
    IFeedbackService feedbackService;

    @ApiOperation("For structure of feedbacks, only id (Rubric item id), mark, " +
            "subItems.id (Rubric sub item id), subItem.commentId are required, " +
            "additional comment is optional")
    @PostMapping("candidate/save/{projectId}/{candidateId}")
    public JsonResult saveCandidateFeedback(@PathVariable Integer projectId,
                                            @PathVariable Integer candidateId,
                                            @RequestBody List<FeedbackItemVo> feedbacks) {
        feedbackService.saveCandidateFeedback(projectId, candidateId, feedbacks);
        return JsonResult.ok();
    }

    @PostMapping("candidate/save/general/{projectId}/{candidateId}")
    public JsonResult saveCandidateGeneralComment(@PathVariable Integer projectId,
                                                  @PathVariable Integer candidateId,
                                                  @RequestBody FeedbackGeneralVo generalFeedback) {
        feedbackService.saveCandidateGeneralFeedback(projectId, candidateId, generalFeedback.getGeneralComment());
        return JsonResult.ok();
    }

    @ApiOperation("For structure of feedbacks, only id (Rubric item id), mark, " +
            "subItems.id (Rubric sub item id), subItem.commentId are required," +
            "additional comment is optional")
    @PostMapping("team/save/{projectId}/{teamId}")
    public JsonResult saveTeamFeedback(@PathVariable Integer projectId,
                                       @PathVariable Integer teamId,
                                       @RequestBody List<FeedbackItemVo> feedbacks) {
        feedbackService.saveTeamFeedback(projectId, teamId, feedbacks);
        return JsonResult.ok();
    }

    @ApiOperation("For structure of personalFeedbacks, only candidateId and feedback (content) are required")
    @PostMapping("personal/save/{projectId}/{teamId}")
    public JsonResult savePersonalFeedback(@PathVariable Integer projectId,
                                       @PathVariable Integer teamId,
                                       @RequestBody List<FeedbackTeamPersonal> personalFeedbacks) {
        feedbackService.savePersonalFeedback(projectId, teamId, personalFeedbacks);
        return JsonResult.ok();
    }

    @PostMapping("team/save/general/{projectId}/{teamId}")
    public JsonResult saveTeamGeneralComment(@PathVariable Integer projectId,
                                                  @PathVariable Integer teamId,
                                                  @RequestBody FeedbackGeneralVo generalFeedback) {
        feedbackService.saveTeamGeneralFeedback(projectId, teamId, generalFeedback.getGeneralComment());
        return JsonResult.ok();
    }

    @GetMapping("candidate/get/{projectId}/{candidateId}")
    public JsonResult getCandidateFeedback(@PathVariable Integer projectId,
                                            @PathVariable Integer candidateId) {
        FeedbackCandidateVo candidateFeedback = feedbackService.getCandidateFeedback(projectId, candidateId);
        return JsonResult.ok().data("feedback", candidateFeedback);
    }

    @GetMapping("team/get/{projectId}/{teamId}")
    public JsonResult getTeamFeedback(@PathVariable Integer projectId,
                                           @PathVariable Integer teamId) {
        FeedbackTeamVo teamFeedback = feedbackService.getTeamFeedback(projectId, teamId);
        return JsonResult.ok().data("feedback", teamFeedback);
    }

    @DeleteMapping("candidate/clear/{projectId}/{candidateId}")
    public JsonResult clearCandidateFeedback(@PathVariable Integer projectId,
                                             @PathVariable Integer candidateId) {
        feedbackService.clearCandidateFeedback(projectId, candidateId);
        return JsonResult.ok();
    }

    @DeleteMapping("team/clear/{projectId}/{teamId}")
    public JsonResult clearTeamFeedback(@PathVariable Integer projectId,
                                             @PathVariable Integer teamId) {
        feedbackService.clearTeamFeedback(projectId, teamId);
        return JsonResult.ok();
    }

    @GetMapping("candidate/check/{projectId}/{candidateId}")
    public JsonResult checkCandidateMark(@PathVariable Integer projectId,
                                         @PathVariable Integer candidateId) {
        boolean flag = feedbackService.checkCandidateMark(projectId, candidateId);
        return JsonResult.ok().data("hasMark", flag);
    }

    @GetMapping("team/check/{projectId}/{teamId}")
    public JsonResult checkTeamMark(@PathVariable Integer projectId,
                                         @PathVariable Integer teamId) {
        boolean flag = feedbackService.checkTeamMark(projectId, teamId);
        return JsonResult.ok().data("hasMark", flag);
    }
}
