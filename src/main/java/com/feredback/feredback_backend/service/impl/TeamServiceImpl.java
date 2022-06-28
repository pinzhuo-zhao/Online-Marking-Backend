package com.feredback.feredback_backend.service.impl;

;
import com.feredback.feredback_backend.entity.Candidate;
import com.feredback.feredback_backend.entity.Project;
import com.feredback.feredback_backend.entity.Team;
import com.feredback.feredback_backend.entity.vo.CandidateVo;
import com.feredback.feredback_backend.entity.vo.TeamVo;
import com.feredback.feredback_backend.mapper.*;
import com.feredback.feredback_backend.service.ITeamService;
import com.feredback.feredback_backend.service.ex.DataModificationException;
import com.feredback.feredback_backend.service.ex.InsertionDuplicatedException;
import com.feredback.feredback_backend.util.SecurityContextUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: FE-Redback
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2022-04-17 00:49
 **/

@Service
public class TeamServiceImpl implements ITeamService {

    @Autowired
    private TeamMapper teamMapper;

    @Autowired
    private CandidateMapper candidateMapper;

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SubjectMapper subjectMapper;

    private void ifProjectBelongsToSubject(Integer projectId) {
        Project projectFound = projectMapper.findProjectById(projectId);
        if (projectFound == null) {
            throw new DataModificationException("The project cannot be found");
        }
        Integer isIndividual = projectFound.getIsIndividual();
        if (isIndividual == 1) {
            throw new DataModificationException("This project is not a group project");
        }
        Integer userId = SecurityContextUtil.getCurrentUser().getId();
        List<Integer> subjectIds = userMapper.findSubjectIdsByUserId(userId);

        //if the project provided does not belong to the user, throw exception
        if (!subjectIds.contains(projectFound.getSubjectId())) {
            throw new DataModificationException("You do not have access to this project");
        }
    }

    private void ifTeamBelongsToSubject(Integer teamId) {
        Team teamFound = teamMapper.findTeamById(teamId);
        if (teamFound == null) {
            throw new DataModificationException(teamId + " does not exist");
        }
        //make sure a tutor can only check the teams belong to their subject
        Integer teamSubjectId = teamFound.getSubjectId();
        Integer userId = SecurityContextUtil.getCurrentUser().getId();
        List<Integer> subjectIds = userMapper.findSubjectIdsByUserId(userId);
        if (!subjectIds.contains(teamSubjectId)) {
            throw new DataModificationException("You do not have access to this team: " + teamId);
        }
    }

    @Override
    public void createTeam(TeamVo teamVo, Integer projectId, Integer subjectId) {
        Team teamFound = findTeamByName(teamVo.getTeam().getTeamName());
        if (teamFound != null) {
            throw new InsertionDuplicatedException("There is already a team with this name");
        }
        ifProjectBelongsToSubject(projectId);
        Date now = new Date();
        teamVo.getTeam().setCreateTime(now);
        teamVo.getTeam().setUpdateTime(now);
        teamVo.getTeam().setSubjectId(subjectId);
        teamMapper.insertTeam(teamVo.getTeam());
        Integer rowsAffected = teamMapper.addTeamToProject(teamVo.getTeam().getId(), projectId, now, now);
        //Add selected students to the team when creating the team
        List<CandidateVo> candidates = teamVo.getCandidates();
        List<Integer> candidateIds = new ArrayList<>();
        if (!candidates.isEmpty()) {
            for (CandidateVo candidate : candidates) {
                candidateIds.add(candidate.getId());
            }
            addCandidatesToTeam(candidateIds,teamVo.getTeam().getId());
        }
        if (rowsAffected != 1) {
            throw new DataModificationException("There is an error adding this team");
        }

    }
    @Override
    public Team findTeamByName(String name) {
        Team teamFound = teamMapper.findTeamByName(name);
        return teamFound;
    }

    @Override
    public List<TeamVo> getAllTeamsInSubject(Integer subjectId) {
        Integer userId = SecurityContextUtil.getCurrentUser().getId();
        List<Integer> subjectIds = userMapper.findSubjectIdsByUserId(userId);
        if (!subjectIds.contains(subjectId)) {
            throw new DataModificationException("You do not have access to this subject");
        }
        List<Team> teams = teamMapper.findAllTeamsInSubject(subjectId);
        List<TeamVo> teamVos = new ArrayList<>();
        for (Team team : teams) {
            List<CandidateVo> candidatesInTeam = getCandidatesInTeam(team.getId());
            TeamVo teamVo = new TeamVo();
            teamVo.setTeam(team);
            teamVo.setCandidates(candidatesInTeam);
            teamVos.add(teamVo);
        }
        return teamVos;
    }

    @Override
    public List<TeamVo> getAllTeamsInProject(Integer projectId) {
        ifProjectBelongsToSubject(projectId);
        List<Integer> teamIds = projectMapper.findAllTeamsInProject(projectId);
        List<TeamVo> teamVos = new ArrayList<>();
        for (Integer teamId : teamIds) {
            List<CandidateVo> candidatesInTeam = getCandidatesInTeam(teamId);
            Team team = teamMapper.findTeamById(teamId);
            TeamVo teamVo = new TeamVo();
            teamVo.setTeam(team);
            teamVo.setCandidates(candidatesInTeam);
            teamVos.add(teamVo);
        }
        return teamVos;
    }

    @Override
    public List<TeamVo> getAvailableTeamsForProject(Integer projectId) {
        ifProjectBelongsToSubject(projectId);
        Project projectFound = projectMapper.findProjectById(projectId);
        List<TeamVo> allTeamsInSubject = getAllTeamsInSubject(projectFound.getSubjectId());
        List<TeamVo> allTeamsInProject = getAllTeamsInProject(projectId);
        //filter out those teams already in this project
        allTeamsInSubject.removeAll(allTeamsInProject);
        return allTeamsInSubject;


    }

    @Override
    public int addTeamToProject(Integer projectId, List<Integer> teamIds) {
        ifProjectBelongsToSubject(projectId);
        Project project = projectMapper.findProjectById(projectId);
        Date now = new Date();
        int rowsAdded = 0;
        for (Integer teamId : teamIds) {
            Team team = teamMapper.findTeamById(teamId);
            if (!team.getSubjectId().equals(project.getSubjectId())) {
                throw new DataModificationException(team.getTeamName() + " does not belong to" +
                        "the same subject with project " + project.getProjectDescription());
            }
            ifTeamBelongsToSubject(teamId);
            Integer count = teamMapper.findDuplicatedTeamInProject(teamId, projectId);
            if (count != 0) {
                continue;
            }
            Integer rowsAffected = teamMapper.addTeamToProject(teamId, projectId, now, now);
            rowsAdded++;
            if (rowsAffected != 1) {
                throw new DataModificationException("There is an error adding this team to the project");
            }
        }
        return rowsAdded;
    }
    @Override
    public void removeTeamFromProject(Integer projectId, Integer teamId) {
        ifProjectBelongsToSubject(projectId);
        ifTeamBelongsToSubject(teamId);
        Integer rowsAffected = teamMapper.removeTeamFromProject(projectId, teamId, new Date());
        if (rowsAffected != 1) {
            throw new DataModificationException("There is an error removing this team to the project");
        }

    }


    @Override
    public void clearCandidateInTeam(Integer teamId) {
        ifTeamBelongsToSubject(teamId);
        candidateMapper.clearCandidatesInTeam(teamId);

    }

    @Override
    public Integer addCandidatesToTeam(List<Integer> candidateIds, Integer teamId) {
        ifTeamBelongsToSubject(teamId);
        int count = 0;
        for (Integer candidateId:candidateIds) {
            //skip the insertion if there is already a pair of candidate and team in the db
            Integer recordsFound = teamMapper.findDuplicatedCandidateInTeam(candidateId, teamId);
            if (recordsFound != null ) {
                continue;
            }

            //skip the insertion if there is no such candidate
            Candidate candidateFound = candidateMapper.findCandidateById(candidateId);
            if (candidateFound == null) {
                continue;
            }
            Date now = new Date();
            teamMapper.addCandidateToTeam(candidateId, teamId, now, now);
            count++;
        }
        return count;
    }

    @Override
    public void removeCandidateFromTeam(Integer teamId, Integer candidateId) {
        ifTeamBelongsToSubject(teamId);
        Integer rowsAffected = teamMapper.deleteCandidateInTeam(teamId, candidateId, new Date());
        if (rowsAffected != 1) {
            throw new DataModificationException("There is an error updating this team");
        }

    }

    @Override
    public List<CandidateVo> getCandidatesInTeam(Integer teamId) {
        ifTeamBelongsToSubject(teamId);
        List<CandidateVo> candidates = teamMapper.getCandidatesInTeam(teamId);
        return candidates;
    }

    @Override
    public List<CandidateVo> getAvailableCandidatesForTeam(Integer projectId, Integer teamId) {
        ifProjectBelongsToSubject(projectId);
        ifTeamBelongsToSubject(teamId);
        Project project = projectMapper.findProjectById(projectId);
        List<TeamVo> teams = getAllTeamsInProject(projectId);
        List<Candidate> candidates = candidateMapper.findAllCandidates(project.getSubjectId());
        List<CandidateVo> candidateVos = new ArrayList<>();
        for (Candidate candidate : candidates) {
            CandidateVo candidateVo = new CandidateVo();
            BeanUtils.copyProperties(candidate,candidateVo);
            candidateVos.add(candidateVo);
        }

        for (TeamVo teamVo : teams) {
            candidateVos.removeAll(teamVo.getCandidates());
        }
        return candidateVos;

    }




    @Override
    public void updateTeam(Team team) {
        Team teamFound = teamMapper.findTeamById(team.getId());
        if (teamFound == null) {
            throw new DataModificationException("There is no such team");
        }
        team.setUpdateTime(new Date());
        Integer rowsAffected = teamMapper.updateTeam(team);
        if (rowsAffected != 1) {
            throw new DataModificationException("There is an error updating this team");
        }

    }

    @Override
    public void deleteTeamById(Integer id) {
        ifTeamBelongsToSubject(id);
        Date updateTime = new Date();
        Integer rowsAffected = teamMapper.deleteTeamById(id, updateTime);
        teamMapper.removeTeamFromAllProjects(id, updateTime);
        teamMapper.removeAllCandidatesFromTeam(id,updateTime);
        if (rowsAffected != 1) {
            throw new DataModificationException("There is an error deleting this team");
        }
    }


}
