package com.feredback.feredback_backend.controller;

import com.feredback.feredback_backend.entity.Team;
import com.feredback.feredback_backend.entity.vo.CandidateVo;
import com.feredback.feredback_backend.entity.vo.TeamVo;
import com.feredback.feredback_backend.service.ITeamService;
import com.feredback.feredback_backend.util.JsonResult;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: FE-Redback
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2022-04-17 00:18
 **/
@RestController
@RequestMapping("api/team")
@CrossOrigin
public class TeamController extends BaseController {

    @Autowired
    ITeamService teamService;


    @ApiOperation(value = "Form needs teamName,with path variable projectId, and an array of" +
            "candidates (only id needed),具体例子看下面的Implementation notes",notes = "{\n" +
            "  \"candidates\": [\n" +
            "    {\n" +
            "      \"id\": 1\n" +
            "    },{\"id\":2}\n" +
            "  ],\n" +
            "  \"team\": {\n" +
            "    \"teamName\": \"A1_Team8\"\n" +
            "  }\n" +
            "}")
    @PostMapping("createTeam/{subjectId}/{projectId}")
    public JsonResult createTeam(@RequestBody TeamVo teamvo, @PathVariable Integer projectId, @PathVariable Integer subjectId) {
        teamService.createTeam(teamvo, projectId, subjectId);
        return JsonResult.ok();
    }


    @GetMapping("getAllTeamsInSubject/{subjectId}")
    public JsonResult getAllTeamsInSubject(@PathVariable Integer subjectId) {
        List<TeamVo> teams = teamService.getAllTeamsInSubject(subjectId);
        return JsonResult.ok().data("teamsInSubject", teams);
    }


    @GetMapping("getAvailableCandidatesForTeam/{projectId}/{teamId}")
    public JsonResult getAvailableCandidatesForTeam(@PathVariable Integer projectId,
                                                    @PathVariable Integer teamId) {
        List<CandidateVo> candidates = teamService.getAvailableCandidatesForTeam(projectId, teamId);
        return JsonResult.ok().data("availableCandidates", candidates);
    }

   /* @ApiOperation("Form needs an array of candidateId: []candidateIds with path variable:teamId")
    @PostMapping("addCandidatesToTeam/{teamId}")
    public JsonResult addCandidatesToTeam(@RequestBody List<Integer> candidateIds,
                                          @PathVariable Integer teamId) {
        Integer rowsAdded = teamService.addCandidatesToTeam(candidateIds, teamId);
        return JsonResult.ok().data("recordsAdded", rowsAdded);
    }*/

    @GetMapping("getCandidatesInTeam/{teamId}")
    public JsonResult getCandidatesInTeam(@PathVariable Integer teamId) {
        List<CandidateVo> candidates = teamService.getCandidatesInTeam(teamId);
        return JsonResult.ok().data("candidatesInTeam", candidates);
    }

    @PostMapping("editCandidatesInTeam/{teamId}")
    public JsonResult editCandidatesInTeam(@RequestBody List<Integer> candidateIds,
                                          @PathVariable Integer teamId) {
        teamService.clearCandidateInTeam(teamId);
        Integer rowsAdded = teamService.addCandidatesToTeam(candidateIds, teamId);
        return JsonResult.ok().data("recordsAdded", rowsAdded);
    }


    /*@PutMapping("removeCandidateFromTeam/{teamId}/{candidateId}")
    public JsonResult removeCandidateFromTeam(@PathVariable Integer teamId,
                                              @PathVariable Integer candidateId) {
        teamService.removeCandidateFromTeam(teamId, candidateId);
        return JsonResult.ok();
    }*/

    @ApiOperation("Form needs teamId and new teamName")
    @PutMapping("updateTeam")
    public JsonResult updateTeam (@RequestBody Team team) {
        teamService.updateTeam(team);
        return JsonResult.ok();
    }

    @ApiOperation(value = "Delete a team by setting its 'isDeleted' to 1 ")
    @PutMapping("delete/{id}")
    public JsonResult deleteTeam(@PathVariable Integer id) {
        teamService.deleteTeamById(id);
        return JsonResult.ok();
    }



}
