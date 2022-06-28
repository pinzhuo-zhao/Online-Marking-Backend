package com.feredback.feredback_backend.entity;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.util.Date;

/**
 * @program: FE-Redback
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2022-04-10 00:53
 **/
@Data
public class User extends BaseEntity {
    /**
     *
     */
    private Integer id;

    /**
     *
     */
    @ApiModelProperty(required = true,notes = "!!")
    private String firstName;

    /**
     *
     */

    private String middleName;

    /**
     *
     */
    @ApiModelProperty(required = true,notes = "!!")
    private String lastName;

    /**
     *
     */
    @ApiModelProperty(required = true,notes = "!!")
    private String email;


    /**
     *
     */
    @ApiModelProperty(required = true,notes = "!!")
    private String password;




    /**
     *
     */
    private Integer isAdmin;

    /**
     *
     */
    private Integer isSubjectCoordinator;

    private Integer isHeadTutor;

    private String verificationCode;

    private Date verificationCodeSentTime;







}
