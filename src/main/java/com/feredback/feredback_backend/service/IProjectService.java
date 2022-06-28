package com.feredback.feredback_backend.service;

import com.feredback.feredback_backend.entity.Candidate;
import com.feredback.feredback_backend.entity.Project;

import java.util.List;

/**
 * @program: FE-Redback
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2022-04-17 22:03
 **/
public interface IProjectService {
    void createProject(Project project);

    List<Project> getAllProjects(Integer subjectId);

    List<Candidate> getCandidatesInProject(Integer projectId);

    void deleteCandidateFromProject(Integer projectId, Integer candidateId);

    void updateProject(Project project);

    void deleteProjectById(Integer id);

    List<Candidate> getAvailableCandidates(Integer projectId);

    int addCandidateToProject(Integer projectId, List<Integer> candidateId);

    List<Project> getProjectsNoTemplate(Integer subjectId);
}
