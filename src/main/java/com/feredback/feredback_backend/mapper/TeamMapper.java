package com.feredback.feredback_backend.mapper;

import com.feredback.feredback_backend.entity.Team;
import com.feredback.feredback_backend.entity.vo.CandidateVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

/**
 * @program: FE-Redback
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2022-04-17 00:51
 **/
@Mapper
public interface TeamMapper {
    Integer insertTeam(Team team);

    Team findTeamByName(String name);

    Team findTeamById(Integer id);



    Integer addCandidateToTeam(Integer candidateId, Integer teamId, Date createTime, Date updateTime);

    Integer findDuplicatedCandidateInTeam(Integer candidateId, Integer teamId);

    Integer updateTeam(Team team);

    Integer deleteTeamById(Integer id, Date updateTime);

    List<CandidateVo> getCandidatesInTeam(Integer teamId);

    Integer deleteCandidateInTeam(Integer teamId, Integer candidateId, Date updateTime);

    Integer addTeamToProject(Integer teamId, Integer projectId, Date createTime, Date updateTime);

    List<Team> findAllTeamsInSubject(Integer subjectId);

    Integer removeTeamFromProject(Integer projectId, Integer teamId, Date updateTime);

    Integer removeTeamFromAllProjects(Integer teamId, Date updateTime);
    Integer removeAllCandidatesFromTeam(Integer teamId, Date updateTime);

    Integer findDuplicatedTeamInProject(Integer teamId, Integer projectId);
}
