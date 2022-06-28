package com.feredback.feredback_backend.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: FE-Redback
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2022-04-10 17:47
 **/

@Data
public class Subject extends BaseEntity {
    private Integer id;

    @ApiModelProperty(required = true,notes = "!!")
    private String subjectCode;

    @ApiModelProperty(required = true,notes = "!!")
    private String subjectName;




}
