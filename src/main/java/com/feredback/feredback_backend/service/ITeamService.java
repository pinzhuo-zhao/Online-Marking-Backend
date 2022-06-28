package com.feredback.feredback_backend.service;

import com.feredback.feredback_backend.entity.Team;
import com.feredback.feredback_backend.entity.vo.CandidateVo;
import com.feredback.feredback_backend.entity.vo.TeamVo;

import java.util.List;

/**
 * @program: FE-Redback
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2022-04-17 00:49
 **/
public interface ITeamService {
    void createTeam(TeamVo team, Integer projectId, Integer subjectId);

    Team findTeamByName(String name);

    List<TeamVo> getAllTeamsInSubject(Integer subjectId);

    List<TeamVo> getAllTeamsInProject(Integer projectId);

    List<TeamVo> getAvailableTeamsForProject(Integer projectId);

    int addTeamToProject(Integer projectId, List<Integer> teamIds);

    void removeTeamFromProject(Integer projectId, Integer teamId);

    Integer addCandidatesToTeam(List<Integer> candidateIds, Integer teamId);

    void updateTeam(Team team);

    void deleteTeamById(Integer id);

    List<CandidateVo> getCandidatesInTeam(Integer teamId);

    void removeCandidateFromTeam(Integer teamId, Integer candidateId);

    List<CandidateVo> getAvailableCandidatesForTeam(Integer teamId, Integer id);

    void clearCandidateInTeam(Integer teamId);
}
