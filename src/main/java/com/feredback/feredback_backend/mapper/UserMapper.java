package com.feredback.feredback_backend.mapper;

import com.feredback.feredback_backend.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

/**
 * @program: FE-Redback
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2022-04-10 16:06
 **/

@Mapper
public interface UserMapper {
    Integer insertUser(User user);

    List<User> getTutorsBySubjectId(Integer subjectId);

    User findUserByEmail(String email);

    Integer setVerificationCode(Integer userId, String code, Date updateTime, Date sentTime);

    Integer updateUserPassword(User user);

    Integer addStaffToSubject(Integer userId, Integer subjectId, Date createTime, Date updateTime);

    Integer findDuplicatedStaffInSubject(Integer userId, Integer subjectId);

    List<Integer> findSubjectIdsByUserId(Integer userId);

    List<User> findAllCoordinators();

    Integer deleteUserById(Integer id, Date updateTime);

    Integer removeUserInSubject(Integer id, Date updateTime);
}
