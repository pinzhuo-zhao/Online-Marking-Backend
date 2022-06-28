package com.feredback.feredback_backend.entity.vo;

import com.feredback.feredback_backend.entity.Subject;
import com.feredback.feredback_backend.entity.User;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: FE-Redback
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2022-05-07 23:59
 **/

@Data
public class CoordinatorVo implements Serializable {

    private User user;
    private Subject subject;
}
