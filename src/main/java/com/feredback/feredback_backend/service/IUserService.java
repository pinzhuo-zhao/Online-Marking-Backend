package com.feredback.feredback_backend.service;

import com.feredback.feredback_backend.entity.User;
import com.feredback.feredback_backend.entity.vo.UserVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @program: FE-Redback
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2022-04-10 15:02
 **/
public interface IUserService {
    void addTutor(User user, Integer subjectId);


    void addCoordinator(User user, Integer subjectId);


    int addTutorByCsv(MultipartFile file, Integer subjectId);

    User findUserByEmail(String email);

    void setVerificationCode(Integer userId, String code);

    void changePassword(User user);

    User preProcessUser(User user);

    List<UserVo> getAllTutors(Integer subjectId);

    boolean verifyCode(User user);

    List<UserVo> getAllCoordinators();

    void deleteUserById(Integer userId);


}
