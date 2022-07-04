package com.feredback.feredback_backend.service;

import com.feredback.feredback_backend.entity.User;

/**
 * @program: Online-Marking-Backend
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2022-07-04 23:46
 **/
public interface ILoginService {
    String login(User user);

    void logout();
}
