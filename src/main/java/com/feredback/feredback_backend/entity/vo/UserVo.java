package com.feredback.feredback_backend.entity.vo;

import com.feredback.feredback_backend.entity.Subject;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @program: FE-Redback
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2022-04-12 23:09
 **/

@Data
public class UserVo implements Serializable {

    /**
     *
     */
    private Integer id;


    /**
     *
     */
    private Integer isAdmin;

    /**
     *
     */
    private Integer isSubjectCoordinator;

    private Integer isHeadTutor;


    /**
     *
     */
    private String firstName;

    /**
     *
     */
    private String middleName;

    /**
     *
     */
    private String lastName;

    /**
     *
     */
    private String email;

    private List<Subject> subjects;


    private static final long serialVersionUID = 1L;
}
