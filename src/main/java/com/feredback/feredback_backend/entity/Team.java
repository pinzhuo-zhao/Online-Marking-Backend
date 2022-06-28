package com.feredback.feredback_backend.entity;

import lombok.Data;

/**
 * @program: FE-Redback
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2022-04-15 20:47
 **/
@Data
public class Team extends BaseEntity{
    private Integer id;
    private String teamName;
    private Integer subjectId;

}
