package com.feredback.feredback_backend.controller;

import com.feredback.feredback_backend.entity.Candidate;
import com.feredback.feredback_backend.entity.Project;
import com.feredback.feredback_backend.entity.vo.TeamVo;
import com.feredback.feredback_backend.entity.vo.TemplateVo;
import com.feredback.feredback_backend.service.IProjectService;
import com.feredback.feredback_backend.service.ITeamService;
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
 * @create: 2022-04-17 21:59
 **/

@RestController
@RequestMapping("api/project")
@CrossOrigin
public class ProjectController extends BaseController{
    @Autowired
    IProjectService projectService;
    @Autowired
    ITeamService teamService;
    @Autowired
    ITemplateService templateService;

    @ApiOperation("Form needs (isIndividual), subjectId, duration, warningTime and projectDescription, let " +
            "the user choose templateId from a dropdown menu" +
            "if there is no desired template, let user choose" +
            " 'create later', so no templateId will be provided，the form should display " +
            "logged in user's subjectId and subjectName")
    @PostMapping("createProject")
    public JsonResult createProject (@RequestBody Project project) {
        projectService.createProject(project);
        return JsonResult.ok();
    }

    @GetMapping("allProjects/{subjectId}")
    public JsonResult getAllProjects(@PathVariable Integer subjectId) {
        List<Project> projects = projectService.getAllProjects(subjectId);
        return JsonResult.ok().data("projectsFromSubject", projects);
    }

    @GetMapping("ProjectsNoTemplate/{subjectId}")
    public JsonResult projectsNoTemplate(@PathVariable Integer subjectId) {
        List<Project> projects = projectService.getProjectsNoTemplate(subjectId);
        return JsonResult.ok().data("projectsNoTemplate", projects);
    }


    @ApiOperation("Form needs project desc, id, is_individual, duration, warningTime")
    @PutMapping("updateProject")
    public JsonResult updateProject (@RequestBody Project project) {
        projectService.updateProject(project);
        return JsonResult.ok();
    }

    @ApiOperation(value = "Delete a project by setting its 'isDeleted' to 1 ")
    @PutMapping("delete/{id}")
    public JsonResult deleteProject(@PathVariable Integer id) {
        projectService.deleteProjectById(id);
        return JsonResult.ok();
    }



    @PostMapping("addCandidateToProject/{projectId}")
    public JsonResult addCandidateToProject(@RequestBody List<Integer> candidateIds, @PathVariable Integer projectId) {
        int i = projectService.addCandidateToProject(projectId, candidateIds);
        return JsonResult.ok().data("rowsAdded",i);
    }

    @GetMapping("getIndividualCandidatesInProject/{projectId}")
    public JsonResult getIndividualCandidatesInProject(@PathVariable Integer projectId) {
        List<Candidate> candidates = projectService.getCandidatesInProject(projectId);
        return JsonResult.ok().data("candidatesInProject",candidates);
    }

    @GetMapping("getAvailableCandidatesForProject/{projectId}")
    public JsonResult getAvailableCandidatesForProject(@PathVariable Integer projectId) {
        List<Candidate> candidates = projectService.getAvailableCandidates(projectId);
        return JsonResult.ok().data("candidatesAvailable",candidates);
    }

    @PutMapping("removeCandidateFromProject/{projectId}/{candidateId}")
    public JsonResult removeCandidateFromProject(@PathVariable Integer projectId, @PathVariable Integer candidateId) {
        projectService.deleteCandidateFromProject(projectId,candidateId);
        return JsonResult.ok();
    }




    @GetMapping("getAllTeamsInProject/{projectId}")
    public JsonResult getAllTeamsInProject(@PathVariable Integer projectId) {
        List<TeamVo> teams = teamService.getAllTeamsInProject(projectId);
        return JsonResult.ok().data("teamsInProject",teams);
    }

    @GetMapping("getAvailableTeamsForProject/{projectId}")
    public JsonResult getAvailableTeamsForProject(@PathVariable Integer projectId) {
        List<TeamVo> teams = teamService.getAvailableTeamsForProject(projectId);
        return JsonResult.ok().data("teamsAvailable",teams);
    }

    @PostMapping("addTeamsToProject/{projectId}")
    public JsonResult addTeamToProject(@PathVariable Integer projectId, @RequestBody List<Integer> teamIds) {
        int i = teamService.addTeamToProject(projectId, teamIds);
        return JsonResult.ok().data("teamsAdded", i);
    }



    @PutMapping("removeTeamFromProject/{projectId}/{teamId}")
    public JsonResult removeTeamToProject(@PathVariable Integer projectId, @PathVariable Integer teamId) {
        teamService.removeTeamFromProject(projectId, teamId);
        return JsonResult.ok();
    }

    @PostMapping("connectTemplateToProject/{projectId}/{templateId}")
    public JsonResult connectTemplateToProject(@PathVariable Integer projectId,
                                               @PathVariable Integer templateId) {
        templateService.connectTemplateToProject(projectId,templateId);
        return JsonResult.ok();
    }

    @GetMapping("showTemplateOfProject/{projectId}")
    public JsonResult showTemplateOfProject(@PathVariable Integer projectId) {
        TemplateVo template = templateService.showTemplateOfProject(projectId);
        return JsonResult.ok().data("projectTemplate", template);
    }

}
