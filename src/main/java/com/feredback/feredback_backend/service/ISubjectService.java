package com.feredback.feredback_backend.service;

import com.feredback.feredback_backend.entity.Subject;

import java.util.List;

/**
 * @program: FE-Redback
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2022-04-15 22:14
 **/
public interface ISubjectService {

    List<Subject> getAllSubjects();


    Subject getSubjectById(Integer id);

    boolean findDuplicatedSubjectCodeOrName(String subjectCode, String subjectName);

    void addSubject(Subject subject);

    List<Subject> getMySubjects();

    void deleteSubject(Integer id);
}
