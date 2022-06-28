package com.feredback.feredback_backend.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @program: FE-Redback
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2022-04-03 20:27
 **/

@Data
public class BaseEntity implements Serializable {
    private Integer isDeleted;
    private Date createTime;
    private Date updateTime;
    private static final long serialVersionUID = 1L;
}
