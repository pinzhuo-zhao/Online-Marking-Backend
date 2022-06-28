package com.feredback.feredback_backend.mapper;

import com.feredback.feredback_backend.entity.Candidate;
import com.feredback.feredback_backend.entity.Project;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @program: FE-Redback
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2022-04-17 22:07
 **/
@Mapper
public interface ProjectMapper {
    void insertProject(Project project);
    Project findProjectById(Integer id);
    List<Project> findProjectByTemplateId(Integer templateId);
    Project findProjectByDescription(@Param("projectDescription") String desc, Integer subjectId);
    List<Project> findAllProjects(Integer subjectId);
    Integer updateProject(Project project);
    Integer deleteProjectById(Integer id, Date updateTime);
    List<Integer> findAllTeamsInProject(Integer projectId);

    Integer addCandidateToProject(Integer projectId, Integer candidateId, Date createTime, Date updateTime);

    List<Candidate> findIndividualCandidatesInProject(Integer projectId);

    Integer deleteCandidateFromProject(Integer projectId, Integer candidateId, Date updateTime);
    Integer removeAllCandidatesFromProject(Integer projectId, Date updateTime);
    Integer removeAllTeamsFromProject(Integer projectId, Date updateTime);


    List<Project> findProjectWithoutTemplate(Integer subjectId);
}
