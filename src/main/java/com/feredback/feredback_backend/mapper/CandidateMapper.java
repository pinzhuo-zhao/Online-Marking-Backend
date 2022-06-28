package com.feredback.feredback_backend.mapper;

import com.feredback.feredback_backend.entity.Candidate;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

/**
* @author pinzhuozhao
* @description
* @createDate 2022-04-03 23:22:50
* @Entity com.feredback.feredback_backend.entity.Candidate
*/

@Mapper
public interface CandidateMapper {
    Integer insertCandidate(Candidate candidate);
    Candidate findByEmail(String email);
    Candidate findCandidateById(Integer id);
    List<Candidate> findCandidatesByTeamId(Integer teamId);
    List<Candidate> findAllCandidates(Integer subjectId);


    Integer deleteCandidateById(Integer id, Date updateTime);
    Candidate findCandidateByEmailAndId(String email, Integer id);

    Integer findDuplicatedCandidateInSubject(Integer candidateId, Integer subjectId);
    Integer removeCandidateFromAllSubjects(Integer candidateId, Date updateTime);
    Integer removeCandidateFromAllTeams(Integer candidateId, Date updateTime);
    Integer removeCandidateFromAllProjects(Integer candidateId, Date updateTime);

    Integer findDuplicatedCandidateInProject(Integer candidateId, Integer projectId);

    void clearCandidatesInTeam(Integer teamId);
}
