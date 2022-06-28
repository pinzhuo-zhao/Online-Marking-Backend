package com.feredback.feredback_backend.mapper;

import com.feredback.feredback_backend.entity.Subject;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

/**
 * @program: FE-Redback
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2022-04-10 17:40
 **/
@Mapper
public interface SubjectMapper {
    Subject findSubjectById(Integer id);

    List<Subject> getAllSubjects();

    Integer insertSubject(Subject subject);

    Subject findSubjectByCode(String subjectCode, String subjectName);

    List<Subject> getSubjectsByUserId(Integer userId);

    Integer deleteSubjectById(Integer id, Date updateTime);

    Integer insertCandidateToSubject(Integer candidateId, Integer subjectId, Date createTime, Date updateTime);

    Integer removeCandidatesFromSubject(Integer subjectId, Date updateTime);

    Integer removeUsersFromSubject(Integer subjectId, Date updateTime);

}
