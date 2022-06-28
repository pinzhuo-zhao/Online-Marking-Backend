package com.feredback.feredback_backend.service.impl;

import com.feredback.feredback_backend.entity.Project;
import com.feredback.feredback_backend.entity.Subject;
import com.feredback.feredback_backend.mapper.ProjectMapper;
import com.feredback.feredback_backend.mapper.SubjectMapper;
import com.feredback.feredback_backend.mapper.UserMapper;
import com.feredback.feredback_backend.service.ISubjectService;
import com.feredback.feredback_backend.service.ex.DataModificationException;
import com.feredback.feredback_backend.service.ex.InsertionDuplicatedException;
import com.feredback.feredback_backend.util.SecurityContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @program: FE-Redback
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2022-04-15 22:15
 **/
@Service
public class SubjectServiceImpl implements ISubjectService {
    @Autowired
    private SubjectMapper subjectMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ProjectMapper projectMapper;



    @Override
    public List<Subject> getAllSubjects() {
        List<Subject> subjects = subjectMapper.getAllSubjects();
        return subjects;
    }

    @Override
    public List<Subject> getMySubjects() {
        Integer userId = SecurityContextUtil.getCurrentUser().getId();
        List<Subject> subjectsByUserId = subjectMapper.getSubjectsByUserId(userId);
        return subjectsByUserId;

    }

    @Override
    public Subject getSubjectById(Integer id) {
        Integer userId = SecurityContextUtil.getCurrentUser().getId();
        List<Integer> subjectIds = userMapper.findSubjectIdsByUserId(userId);
        if (!subjectIds.contains(id)) {
            throw new DataModificationException("You are not allowed to access this subject");
        }
        return subjectMapper.findSubjectById(id);
    }

    @Override
    public boolean findDuplicatedSubjectCodeOrName(String subjectCode, String subjectName) {
        Subject subject = subjectMapper.findSubjectByCode(subjectCode, subjectName);
        return subject != null;

    }

    @Override
    public void addSubject(Subject subject) {
        boolean isDuplicated = findDuplicatedSubjectCodeOrName(subject.getSubjectCode(), subject.getSubjectName());
        if (isDuplicated) {
            throw new InsertionDuplicatedException("This subject's code or name already exists, please check again");
        }
        Date now = new Date();
        subject.setCreateTime(now);
        subject.setUpdateTime(now);
        Integer rowsAffected = subjectMapper.insertSubject(subject);
        if (rowsAffected != 1) {
            throw new DataModificationException("There is an error adding this subject");
        }
    }

    @Override
    public void deleteSubject(Integer id) {
        Date now = new Date();
        Integer rowsAffected = subjectMapper.deleteSubjectById(id, now);
        subjectMapper.removeCandidatesFromSubject(id, now);
        subjectMapper.removeUsersFromSubject(id,now);
        List<Project> allProjects = projectMapper.findAllProjects(id);
        for (Project project : allProjects) {
            projectMapper.deleteProjectById(project.getId(), now);
        }
        if (rowsAffected != 1) {
            throw new DataModificationException("There is an error adding this subject");
        }

    }


}
