package com.feredback.feredback_backend.service.impl;

import com.feredback.feredback_backend.entity.Candidate;
import com.feredback.feredback_backend.entity.Project;
import com.feredback.feredback_backend.mapper.CandidateMapper;
import com.feredback.feredback_backend.mapper.ProjectMapper;
import com.feredback.feredback_backend.mapper.UserMapper;
import com.feredback.feredback_backend.service.IProjectService;
import com.feredback.feredback_backend.service.ex.DataModificationException;
import com.feredback.feredback_backend.service.ex.InsertionDuplicatedException;
import com.feredback.feredback_backend.util.SecurityContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: FE-Redback
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2022-04-17 22:03
 **/
@Service
public class ProjectServiceImpl implements IProjectService {

    @Autowired
    ProjectMapper projectMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CandidateMapper candidateMapper;



    private void ifProjectBelongsToSubject(Integer projectId) {
        Project projectFound = projectMapper.findProjectById(projectId);
        Integer userId = SecurityContextUtil.getCurrentUser().getId();
        List<Integer> subjectIds = userMapper.findSubjectIdsByUserId(userId);

        if (projectFound == null) {
            throw new DataModificationException("The project cannot be found");
        }
        if (!subjectIds.contains(projectFound.getSubjectId())) {
            System.out.println(projectFound.getSubjectId());
            System.out.println(subjectIds);
            throw new DataModificationException("You do not have access to this project");
        }
    }

    private void ifCandidateBelongsToSubject(Integer candidateId) {
        Candidate candidateFound = candidateMapper.findCandidateById(candidateId);
        Integer userId = SecurityContextUtil.getCurrentUser().getId();
        List<Integer> subjectIds = userMapper.findSubjectIdsByUserId(userId);
        List<Integer> candidateIds = new ArrayList<>();
        for (Integer subjectId : subjectIds) {
            List<Candidate> candidates = candidateMapper.findAllCandidates(subjectId);
            for (Candidate candidate : candidates) {
                candidateIds.add(candidate.getId());
            }
        }
        if (candidateFound == null) {
            throw new DataModificationException("The student cannot be found");
        }
        if (!candidateIds.contains(candidateId)) {
            throw new DataModificationException("You do not have access to this student");
        }
    }

    @Override
    public void createProject(Project project) {
        Project projectFound = projectMapper.findProjectByDescription(project.getProjectDescription(),
                project.getSubjectId());
        if (projectFound != null) {
            throw new InsertionDuplicatedException(
                    "There is already a project with the same description");
        }
        Date now = new Date();
        project.setCreateTime(now);
        project.setUpdateTime(now);
        projectMapper.insertProject(project);
    }

    @Override
    public List<Project> getAllProjects(Integer subjectId) {
        Integer userId = SecurityContextUtil.getCurrentUser().getId();
        List<Integer> subjectIds = userMapper.findSubjectIdsByUserId(userId);
        if (!subjectIds.contains(subjectId)) {
            throw new DataModificationException("You do not have access to this subject");
        }
        return projectMapper.findAllProjects(subjectId);
    }

    @Override
    public List<Candidate> getAvailableCandidates(Integer projectId) {
        ifProjectBelongsToSubject(projectId);
        Project projectFound = projectMapper.findProjectById(projectId);
        if (projectFound.getIsIndividual() == 0) {
            throw new DataModificationException("This project is a group project");
        }
        //remove candidates who have already been in this project
        List<Candidate> allCandidates = candidateMapper.findAllCandidates(projectFound.getSubjectId());
        List<Candidate> candidatesInProject = projectMapper.findIndividualCandidatesInProject(projectId);
        allCandidates.removeAll(candidatesInProject);
        return allCandidates;


    }


    @Override
    public int addCandidateToProject(Integer projectId, List<Integer> candidateIds) {
        Project projectFound = projectMapper.findProjectById(projectId);
        if (projectFound.getIsIndividual() == 0) {
            throw new DataModificationException("This project is not an individual project");
        }
        ifProjectBelongsToSubject(projectId);
        Date now = new Date();
        int rowsAdded = 0;
        for (Integer candidateId : candidateIds) {
            ifCandidateBelongsToSubject(candidateId);
            Integer count = candidateMapper.findDuplicatedCandidateInProject(candidateId, projectId);
            if (count != 0) {
                continue;
            }
            Integer rowsAffected = projectMapper.addCandidateToProject(projectId, candidateId, now, now);
            rowsAdded++;
            if (rowsAffected != 1) {
                throw new DataModificationException("There is an error adding this candidate to the project");
            }
        }
        return rowsAdded;
    }

    @Override
    public List<Project> getProjectsNoTemplate(Integer subjectId) {
        Integer userId = SecurityContextUtil.getCurrentUser().getId();
        List<Integer> subjectIds = userMapper.findSubjectIdsByUserId(userId);
        if (!subjectIds.contains(subjectId)) {
            throw new DataModificationException("You do not have access to this project");
        }
        return projectMapper.findProjectWithoutTemplate(subjectId);

    }

    @Override
    public List<Candidate> getCandidatesInProject(Integer projectId) {
        Project projectFound = projectMapper.findProjectById(projectId);
        if (projectFound.getIsIndividual() == 0) {
            throw new DataModificationException("This project is a group project");
        }
        ifProjectBelongsToSubject(projectId);
        List<Candidate> candidates = projectMapper.findIndividualCandidatesInProject(projectId);
        return candidates;
    }


    @Override
    public void deleteCandidateFromProject(Integer projectId, Integer candidateId) {
        Project projectFound = projectMapper.findProjectById(projectId);
        if (projectFound.getIsIndividual() == 0) {
            throw new DataModificationException("This project is a group project");
        }
        ifProjectBelongsToSubject(projectId);
        ifCandidateBelongsToSubject(candidateId);
        Date now = new Date();
        projectMapper.deleteCandidateFromProject(projectId, candidateId, now);
    }

    @Override
    public void updateProject(Project project) {
        ifProjectBelongsToSubject(project.getId());
        project.setUpdateTime(new Date());
        Integer rowsAffected = projectMapper.updateProject(project);
        if (rowsAffected != 1) {
            throw new DataModificationException("There is an error editing this project");
        }

    }

    @Override
    public void deleteProjectById(Integer id) {
        ifProjectBelongsToSubject(id);
        Date updateTime = new Date();
        Integer rowsAffected = projectMapper.deleteProjectById(id, updateTime);
        projectMapper.removeAllTeamsFromProject(id,updateTime);
        projectMapper.removeAllCandidatesFromProject(id,updateTime);
        if (rowsAffected != 1) {
            throw new DataModificationException("There is an error deleting this project");
        }
    }


}
