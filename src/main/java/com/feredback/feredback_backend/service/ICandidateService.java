package com.feredback.feredback_backend.service;


import com.feredback.feredback_backend.entity.Candidate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @program: FE-Redback
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2022-04-03 23:29
 **/
public interface ICandidateService {
    void addCandidate(Candidate candidate, Integer subjectId);

    List<Candidate> getAllUsers(Integer subjectId);

    Integer addCandidateByCsv(MultipartFile file, Integer subjectId);

    Candidate findCandidateByEmail(String email);
    Candidate findCandidateById(Integer id);

    void deleteCandidateById(Integer id);
}
